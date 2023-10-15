package com.rudy.ryanto.core.wallet.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rudy.ryanto.core.wallet.domain.WalletReq;
import com.rudy.ryanto.core.wallet.entity.MasterWallet;
import com.rudy.ryanto.core.wallet.entity.WalletHistory;
import com.rudy.ryanto.core.wallet.entity.WalletHistoryDetail;
import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.repository.WalletHistoryDetailRepository;
import com.rudy.ryanto.core.wallet.repository.WalletHistoryRepository;
import com.rudy.ryanto.core.wallet.repository.WalletMasterRepository;
import com.rudy.ryanto.core.wallet.service.AuditService;
import com.rudy.ryanto.core.wallet.service.WalletService;
import com.rudy.ryanto.core.wallet.util.HistoryGenerator;
import com.rudy.ryanto.core.wallet.util.SeqGenerator;
import com.rudy.ryanto.core.wallet.util.WalletConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WalletService.class})
@TestPropertySource(locations = "classpath:application.properties")
@Slf4j
public class WalletServiceTest {

    private static final BigDecimal MIN_SALDO = new BigDecimal(5_000);

    @MockBean
    private WalletMasterRepository walletMasterRepository;
    @MockBean
    private WalletHistoryRepository walletHistoryRepository;
    @MockBean
    private WalletHistoryDetailRepository walletHistoryDetailRepository;
    @MockBean
    private SeqGenerator seqGenerator;
    @MockBean
    private AuditService auditService;
    @MockBean
    private HistoryGenerator historyGenerator;
    @Autowired
    private WalletService walletService;
    @MockBean
    private RedisTemplate<String, Object> redisTemplate;
    @MockBean
    private ObjectMapper objectMapper;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    private ObjectMapper objectMapper2;

    @Before
    public void setup() {
        doNothing().when(auditService).sendAudit(any());
        when(walletMasterRepository.findByUserId(any())).thenReturn(masterWallet());
        when(walletHistoryRepository.findByWalletId(any())).thenReturn(walleHistory());
        when(walletHistoryDetailRepository.findByIdMaster(any())).thenReturn(walleHistoryDetail());
        valueOperations.set(WalletConstant.CACHES_WALLET.MIN_SISA_SALDO.getCacheName(), new BigDecimal(10_000));
        objectMapper2 = new ObjectMapper();
        doNothing().when(historyGenerator).doAccountingCredit(any(), any(), any(), any());
        doNothing().when(historyGenerator).doAccountingDebit(any(), any(), any(), any());
        when(historyGenerator.doPost()).thenReturn(Boolean.TRUE);
    }

    private WalletReq reqNewWallet() {
        return WalletReq.builder()
                .id(1L)
                .userId(1L)
                .clientId("1001")
                .build();
    }


    private MasterWallet masterWallet() {
        return MasterWallet.builder()
                .id(1L)
                .userId(1L)
                .norek("202301")
                .walletName(WalletConstant.TITLE_REK.concat(String.valueOf(reqNewWallet().getUserId())))
                .saldo(new BigDecimal(0))
                .createBy(reqNewWallet().getClientId())
                .currencyCode(WalletConstant.CURRENCY_CODE.IDR.getCode())
                .status(WalletConstant.STATUS.ACTIVE)
                .build();
    }

    private WalletHistoryDetail walleHistoryDetail() {
        return WalletHistoryDetail.builder()
                .id(1L)
                .idMaster(1L)
                .norek(masterWallet().getNorek())
                .build();
    }

    private WalletHistory walleHistory() {
        return WalletHistory.builder()
                .id(1L)
                .walletId(masterWallet().getId())
                .status(WalletConstant.HISTORY_STATUS.NORMAL)
                .build();
    }

    @Test
    public void createNewWalletTest_success() throws JsonProcessingException {
        when(walletMasterRepository.save(any())).thenReturn(masterWallet());
        var response = walletService.doCreateNew(reqNewWallet());
        assertNotNull(response);
        assertEquals(Optional.of(1L), Optional.ofNullable(response.getMasterWallet().getId()));
    }

    @Test(expected = NullPointerException.class)
    public void createNewWalletTest_failed() throws JsonProcessingException {
        walletService.doCreateNew(null);
    }


    @Test
    public void inquiry_success() throws JsonProcessingException {
        var response = walletService.doInquiry(reqNewWallet());
        assertNotNull(response);
    }

    @Test(expected = CoreWalletException.class)
    public void inquiry_failed() throws JsonProcessingException {
        when(walletMasterRepository.findByUserId(any())).thenThrow(new CoreWalletException(""));
        var response = walletService.doInquiry(reqNewWallet());
        assertNull(response);
    }


    @Test
    public void getHistory_success() throws JsonProcessingException {
        var response = walletService.getHistory(reqNewWallet());
        assertNotNull(response);
    }

    @Test(expected = CoreWalletException.class)
    public void getHistory_failed() throws JsonProcessingException {
        when(walletHistoryRepository.findByWalletId(any())).thenThrow(new NullPointerException());
        walletService.getHistory(reqNewWallet());
    }


    @Test
    public void updateBalance_successSubtract() throws JsonProcessingException {
        var req = reqNewWallet();
        req.setAmount(new BigDecimal(50_000));
        req.setType(WalletConstant.TYPE.SUB.getCode());
        var master = masterWallet();
        master.setSaldo(BigDecimal.valueOf(100_000));
        when(walletMasterRepository.findByUserId(any())).thenReturn(master);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(MIN_SALDO);
        when(walletHistoryRepository.save(any())).thenReturn(walleHistory());
        doNothing().when(historyGenerator).doAccountingCredit(any(), any(), any(), any());
        doNothing().when(historyGenerator).doAccountingDebit(any(), any(), any(), any());
        when(historyGenerator.doPost()).thenReturn(Boolean.TRUE);
        var response = walletService.doUpdateBalance(req);
        assertNotNull(response);
        log.info("response : {}", objectMapper2.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    @Test
    public void updateBalance_successAdd() throws JsonProcessingException {
        var req = reqNewWallet();
        req.setAmount(new BigDecimal(50_000));
        req.setType(WalletConstant.TYPE.ADD.getCode());
        var master = masterWallet();
        master.setSaldo(BigDecimal.valueOf(100_000));
        when(walletMasterRepository.findByUserId(any())).thenReturn(master);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(MIN_SALDO);
        when(walletHistoryRepository.save(any())).thenReturn(walleHistory());
        var response = walletService.doUpdateBalance(req);
        assertNotNull(response);
        log.info("response : {}", objectMapper2.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }


    @Test(expected = CoreWalletException.class)
    public void updateBalance_error() throws JsonProcessingException {
        var req = reqNewWallet();
        req.setAmount(new BigDecimal(50_000));
        req.setType(WalletConstant.TYPE.ADD.getCode());
        var master = masterWallet();
        master.setSaldo(BigDecimal.valueOf(100_000));
        when(walletMasterRepository.findByUserId(any())).thenReturn(master);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(walletHistoryRepository.save(any())).thenReturn(walleHistory());
        var response = walletService.doUpdateBalance(req);
        assertNull(response);
    }


}
