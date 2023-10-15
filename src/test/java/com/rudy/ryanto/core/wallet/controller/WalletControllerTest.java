package com.rudy.ryanto.core.wallet.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rudy.ryanto.core.wallet.domain.WalletReq;
import com.rudy.ryanto.core.wallet.domain.WalletRes;
import com.rudy.ryanto.core.wallet.entity.MasterWallet;
import com.rudy.ryanto.core.wallet.entity.WalletHistory;
import com.rudy.ryanto.core.wallet.entity.WalletHistoryDetail;
import com.rudy.ryanto.core.wallet.service.WalletService;
import com.rudy.ryanto.core.wallet.util.WalletConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WalletController.class)
@Slf4j
@AutoConfigureMockMvc
@DisplayName("Wallet controller Test !")
public class WalletControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WalletService walletService;

    private ObjectMapper objectMapper2;

    @Before
    public void setup() throws JsonProcessingException {
        objectMapper2 = new ObjectMapper();
        objectMapper2.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Mockito.when(walletService.doCreateNew(any())).thenReturn(WalletRes.builder()
                .masterWallet(masterWallet())
                .walletHistoryDetail(walleHistoryDetail())
                .walletHistory(walleHistory())
                .build());
        Mockito.when(walletService.getHistory(any())).thenReturn(WalletRes.builder()
                .masterWallet(masterWallet())
                .walletHistoryDetail(walleHistoryDetail())
                .walletHistory(walleHistory())
                .build());
        Mockito.when(walletService.doUpdateBalance(any())).thenReturn(WalletRes.builder()
                .masterWallet(masterWallet())
                .walletHistoryDetail(walleHistoryDetail())
                .walletHistory(walleHistory())
                .build());
        Mockito.when(walletService.doInquiry(any())).thenReturn(WalletRes.builder()
                .masterWallet(masterWallet())
                .walletHistoryDetail(walleHistoryDetail())
                .walletHistory(walleHistory())
                .build());
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
                .saldo(new BigDecimal(100_000))
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
    public void createNew_success() throws Exception {
        String url = "/api/wallet/v1/create";
        var data = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper2.writeValueAsString(reqNewWallet())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.info("{}", data);
        var response = objectMapper2.readValue(data, WalletRes.class);
    }

    @Test
    public void getHistory() throws Exception {
        String url = "/api/wallet/v1/history";
        var data = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper2.writeValueAsString(reqNewWallet())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.info("{}", data);
        var response = objectMapper2.readValue(data, WalletRes.class);
    }

    @Test
    public void updatebalance_success() throws Exception {
        String url = "/api/wallet/v1/update/balance";
        var data = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper2.writeValueAsString(reqNewWallet())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.info("{}", data);
        var response = objectMapper2.readValue(data, WalletRes.class);
    }

    @Test
    public void inquiry_success() throws Exception {
        String url = "/api/wallet/v1/inquiry";
        var data = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper2.writeValueAsString(reqNewWallet())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.info("{}", data);
        var response = objectMapper2.readValue(data, WalletRes.class);
    }


}