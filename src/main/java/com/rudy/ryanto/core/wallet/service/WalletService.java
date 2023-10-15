package com.rudy.ryanto.core.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rudy.ryanto.core.wallet.domain.AuditData;
import com.rudy.ryanto.core.wallet.domain.WalletReq;
import com.rudy.ryanto.core.wallet.domain.WalletRes;
import com.rudy.ryanto.core.wallet.entity.MasterWallet;
import com.rudy.ryanto.core.wallet.entity.WalletHistory;
import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.repository.WalletHistoryDetailRepository;
import com.rudy.ryanto.core.wallet.repository.WalletHistoryRepository;
import com.rudy.ryanto.core.wallet.repository.WalletMasterRepository;
import com.rudy.ryanto.core.wallet.util.HistoryGenerator;
import com.rudy.ryanto.core.wallet.util.SeqGenerator;
import com.rudy.ryanto.core.wallet.util.WalletConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class WalletService {

    @Autowired
    private WalletMasterRepository walletMasterRepository;

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    @Autowired
    private WalletHistoryDetailRepository walletHistoryDetailRepository;

    @Autowired
    private SeqGenerator seqGenerator;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoryGenerator historyGenerator;


    public WalletRes doCreateNew(WalletReq req) throws JsonProcessingException {
        log.info("doCreateNew : {}", req);
        WalletRes response = new WalletRes();
        try {
            if (null == req)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
            var master = walletMasterRepository.save(MasterWallet.builder()
                    .norek(seqGenerator.generateSequence(String.valueOf(req.getUserId())))
                    .createDate(new Date())
                    .saldo(new BigDecimal(0))
                    .currencyCode(WalletConstant.CURRENCY_CODE.IDR.getCode())
                    .walletName(WalletConstant.TITLE_REK + req.getUserId())
                    .description(WalletConstant.TITLE_REK + req.getUserId() + " - " + req.getClientId())
                    .status(WalletConstant.STATUS.ACTIVE)
                    .createBy(req.getClientId())
                    .build());
            response.setMasterWallet(master);
        } catch (Exception e) {
            log.error("error caused :", e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        } finally {
            doSendAudit(String.valueOf(WalletConstant.STAGES.SUBMIT), WalletConstant.FLOW_WALLET.CREATE_NEW.getCode(), req, response, new BigDecimal(0));
        }
        return response;
    }

    @Transactional(readOnly = true)
    public WalletRes doInquiry(WalletReq req) throws JsonProcessingException {
        log.info("do inquiry : {}", req);
        WalletRes res = null;
        try {
            var master = walletMasterRepository.findByUserId(req.getUserId());
            if (null == master)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.DATA_NOT_FOUND.getDescription());
            res = WalletRes.builder()
                    .masterWallet(master)
                    .build();
        } catch (Exception e) {
            log.error("error causde : ", e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        } finally {
            doSendAudit(WalletConstant.STAGES.INQUIRY.name(), WalletConstant.FLOW_WALLET.INQUIRY.getCode(), req, res, null);
        }

        return res;
    }

    private void doSendAudit(String stage, String flow, WalletReq req, WalletRes res, BigDecimal amount) throws JsonProcessingException {
        auditService.sendAudit(AuditData.builder()
                .userId(String.valueOf(req.getUserId()))
                .amount(String.valueOf(amount))
                .stage(stage)
                .flowName(flow)
                .optionalDetailsData(objectMapper.writeValueAsString(req))
                .optionalDetailsData2(objectMapper.writeValueAsString(res))
                .clientId(req.getClientId())
                .createBy(req.getClientId())
                .createDate(LocalDateTime.now())
                .build());
    }

    @Transactional(readOnly = true)
    public WalletRes getHistory(WalletReq req) throws JsonProcessingException {
        log.info("get history : {}", req);
        WalletRes res = null;
        try {
            var history = walletHistoryRepository.findByWalletId(req.getId());
            if (null == history)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.DATA_NOT_FOUND.getDescription());
            var detail = walletHistoryDetailRepository.findByIdMaster(history.getId());
            if (null == detail)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.DATA_NOT_FOUND.getDescription());
            res = WalletRes.builder()
                    .walletHistory(history)
                    .walletHistoryDetail(detail)
                    .build();
        } catch (Exception e) {
            log.error("error caused : ", e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        } finally {
            doSendAudit(WalletConstant.STAGES.INQUIRY.name(), WalletConstant.FLOW_WALLET.GET_HISTORY.getCode(), req, res, null);
        }
        return res;
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(readOnly = false)
    public WalletRes doUpdateBalance(WalletReq req) throws JsonProcessingException {
        log.info("updating ballance : {}", req);
        WalletRes res = null;
        try {
            var master = walletMasterRepository.findByUserId(req.getUserId());
            if (null == master) {
                log.error("master wallet not found !");
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.DATA_NOT_FOUND.getDescription());
            }
            if (null != req.getAmount() && req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Invalid request amount is {} ", req.getAmount());
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.INVALID_AMOUNT.getDescription());
            }
            var redis = redisTemplate.opsForValue();
            BigDecimal minSisaSaldoAfterTrx = (BigDecimal) redis.get(WalletConstant.CACHES_WALLET.MIN_SISA_SALDO.getCacheName());
            if (null == minSisaSaldoAfterTrx || minSisaSaldoAfterTrx.compareTo(BigDecimal.ZERO) == 0) {
                log.error("parameter SISA SALDO Not Found! Check Redis cache!");
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.PARAMETER_NOT_FOUND.getDescription());
            }
            var beforeSaldo = master.getSaldo();
            if(!StringUtils.isEmpty(req.getType()) && req.getType().equalsIgnoreCase(WalletConstant.TYPE.SUB.getCode())){
                var sisa = master.getSaldo().subtract(req.getAmount());
                log.info("operation : {} saldo : {} param : {} sisa : {}",req.getType(),master.getSaldo(),minSisaSaldoAfterTrx,sisa);
                if (sisa.compareTo(minSisaSaldoAfterTrx) < 0) {
                    log.error("Sisa amount tidak kurang dengan parameter sisa saldo");
                    throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.INVALID_AMOUNT.getDescription());
                }
                master.setSaldo(master.getSaldo().subtract(req.getAmount()));
            }else{
                master.setSaldo(master.getSaldo().add(req.getAmount()));
            }

            walletMasterRepository.save(master);
            var afterSaldo = master.getSaldo();
            if (beforeSaldo.compareTo(afterSaldo) != 0) {
                var masterHistory = walletHistoryRepository.save(WalletHistory.builder()
                        .createBy(req.getClientId())
                        .createDate(new Date())
                        .walletId(master.getId())
                        .status(WalletConstant.HISTORY_STATUS.NORMAL)
                        .description(WalletConstant.FLOW_WALLET.UPDATE_BALANCE.getDesc())
                        .build());
                historyGenerator.doAccountingCredit(req.getNorek(), true, req.getAmount(), masterHistory);
                historyGenerator.doAccountingDebit(req.getNorek(), true, req.getAmount(), masterHistory);
                var isSuccess = historyGenerator.doPost();
                if (!isSuccess) {
                    log.error("failed to save detail ! ");
                    throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
                }
            }
            res = WalletRes.builder()
                    .masterWallet(master)
                    .build();
        } catch (Exception e) {
            log.error("Error caused : ", e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        } finally {
            doSendAudit(WalletConstant.STAGES.SUBMIT.name(), WalletConstant.FLOW_WALLET.UPDATE_BALANCE.getCode(), req, res, req.getAmount());
        }
        return res;
    }
}
