package com.rudy.ryanto.core.wallet.util;

import com.rudy.ryanto.core.wallet.domain.AccountingDto;
import com.rudy.ryanto.core.wallet.entity.WalletHistory;
import com.rudy.ryanto.core.wallet.entity.WalletHistoryDetail;
import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.repository.WalletHistoryDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = HistoryGenerator.class)
@TestPropertySource(locations = "classpath:application.properties")
@Slf4j
public class HistoryGeneratorTest {
    @MockBean
    private WalletHistoryDetailRepository walletHistoryDetailRepository;

    @Autowired
    private HistoryGenerator hg;

    @Before
    public void setup(){
        Mockito.when(walletHistoryDetailRepository.save(any()))
                .thenReturn(WalletHistoryDetail.builder()
                        .id(1L)
                        .idMaster(1L)
                        .build());
    }


    private HashMap<String, AccountingDto> mockBalanceRecord (){
        HashMap<String,AccountingDto> hAll = new HashMap<>();
        hAll.put("123", AccountingDto.builder()
                        .idMaster(1L)
                        .type('D')
                        .norek("123")
                        .amount(new BigDecimal(10_000))
                .build());
        hAll.put("123", AccountingDto.builder()
                .idMaster(1L)
                .type('D')
                .norek("123")
                .amount(new BigDecimal(10_000))
                .build());
        hAll.put("444", AccountingDto.builder()
                .idMaster(1L)
                .type('C')
                .norek("123")
                .amount(new BigDecimal(20_000))
                .build());
        return hAll;
    }

    private WalletHistory walletHistory(Long ids,Long wId){
        return WalletHistory.builder()
                .id(ids)
                .walletId(wId)
                .status(WalletConstant.HISTORY_STATUS.NORMAL)
                .build();
    }

    @Test
    public void accountingCredit_successTest(){
        var mok = mock(HistoryGenerator.class);
        mok.doAccountingCredit("123",true,new BigDecimal(10_000),walletHistory(1L,1L));
        verify(mok,times(1)).doAccountingCredit("123",true,new BigDecimal(10_000),walletHistory(1L,1L));
    }

    @Test
    public void accountingDebit_successTest(){
        var mok = mock(HistoryGenerator.class);
        mok.doAccountingDebit("123",true,new BigDecimal(10_000),walletHistory(1L,1L));
        verify(mok,times(1)).doAccountingDebit("123",true,new BigDecimal(10_000),walletHistory(1L,1L));
    }

    @Test(expected = CoreWalletException.class)
    public void accountingCredit_errorTest(){
        hg.doAccountingCredit(null,true,new BigDecimal(10_000),walletHistory(1L,1L));
    }

    @Test(expected = CoreWalletException.class)
    public void accountingDebit_errorTest(){
        hg.doAccountingDebit(null,true,new BigDecimal(10_000),walletHistory(1L,1L));

    }

    @Test
    public void posting_test(){
        hg.doAccountingCredit("123",true,new BigDecimal(10_000),walletHistory(1L,1L));
        hg.doAccountingDebit("222",true,new BigDecimal(10_000),walletHistory(1L,1L));
        var response = hg.doPost();
        assertNotNull(response);
        assertEquals(response,true);
    }

    @Test(expected = CoreWalletException.class)
    public void posting_error(){
        hg.doAccountingCredit("123",true,new BigDecimal(10_000),walletHistory(1L,1L));
        hg.doAccountingDebit("222",true,new BigDecimal(10_000),walletHistory(1L,1L));
        when(walletHistoryDetailRepository.save(any())).thenThrow(new CoreWalletException(""));
        var response = hg.doPost();
        assertEquals(response,false);
    }

    @Test(expected = CoreWalletException.class)
    public void posting_unbalance(){
        hg.doAccountingCredit("123",true,new BigDecimal(10_000),walletHistory(1L,1L));
        hg.doAccountingDebit("222",true,new BigDecimal(15_000),walletHistory(1L,1L));
        var response = hg.doPost();
    }

}
