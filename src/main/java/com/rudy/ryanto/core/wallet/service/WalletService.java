package com.rudy.ryanto.core.wallet.service;

import com.rudy.ryanto.core.wallet.domain.WalletReq;
import com.rudy.ryanto.core.wallet.domain.WalletRes;
import com.rudy.ryanto.core.wallet.entity.MasterWallet;
import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.repository.WalletHistoryDetailRepository;
import com.rudy.ryanto.core.wallet.repository.WalletHistoryRepository;
import com.rudy.ryanto.core.wallet.repository.WalletMasterRepository;
import com.rudy.ryanto.core.wallet.util.SeqGenerator;
import com.rudy.ryanto.core.wallet.util.WalletConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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


    public WalletRes doCreateNew(WalletReq req, HttpServletRequest servletRequest) {
        log.info("doCreateNew : {}",req);
        WalletRes response = null;
        try{
            if(null==req)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
            walletMasterRepository.save(MasterWallet.builder()
                            .norek(seqGenerator.generateSequence(String.valueOf(req.getUserId())))
                            .createDate(LocalDateTime.now())
                            .saldo(new BigDecimal(0))
                            .currencyCode(WalletConstant.CURRENCY_CODE.IDR.getCode())
                            .walletName(WalletConstant.TITLE_REK+req.getUserId())
                            .description("")
                    .build());
        }catch (Exception e ){
            log.error("error caused :",e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        }finally {

        }
        return response;
    }

    @Transactional(readOnly = true)
    public WalletRes doInquiry(WalletReq req, HttpServletRequest servletRequest) {
        log.info("do inquiry : {}",req);
        WalletRes res;
        try{
            var master = walletMasterRepository.findByUserId(req.getUserId());
            if(null==master)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.DATA_NOT_FOUND.getDescription());
            res = WalletRes.builder()
                    .masterWallet(master)
                    .build();
        }catch (Exception e){
            log.error("error causde : ",e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        }

        return res;
    }

    @Transactional(readOnly = true)
    public WalletRes getHistory(WalletReq req, HttpServletRequest servletRequest) {
        log.info("get history : {}",req);
        WalletRes res;
        try {
            var history = walletHistoryRepository.findByWalletId(req.getId());
            if(null==history)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.DATA_NOT_FOUND.getDescription());
            var detail = walletHistoryDetailRepository.findByIdMaster(history.getId());
            if(null==detail)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.DATA_NOT_FOUND.getDescription());
            res = WalletRes.builder()
                    .walletHistory(history)
                    .walletHistoryDetail(detail)
                    .build();
        }catch (Exception e){
            log.error("error caused : ",e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        }
        return res;
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public WalletRes doUpdateBalance(WalletReq req, HttpServletRequest servletRequest) {
        log.info("updating ballance : {}",req);
        WalletRes res;
        try {
            var master = walletMasterRepository.findByUserId(req.getUserId());
            if(null==master)
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.DATA_NOT_FOUND.getDescription());
            master.setSaldo(master.getSaldo().add(req.getAmount()));
            walletMasterRepository.save(master);
            res = WalletRes.builder()
                    .masterWallet(master)
                    .build();
        }catch (Exception e){
            log.error("Error caused : ",e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        }
        return res;
    }
}
