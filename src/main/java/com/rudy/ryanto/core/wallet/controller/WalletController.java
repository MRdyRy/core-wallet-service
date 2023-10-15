package com.rudy.ryanto.core.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rudy.ryanto.core.wallet.domain.WalletReq;
import com.rudy.ryanto.core.wallet.domain.WalletRes;
import com.rudy.ryanto.core.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/wallet/v1")
@Slf4j
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public WalletRes doCreate(@RequestBody WalletReq req, HttpServletRequest servletRequest) throws JsonProcessingException {
        log.info("/wallet/v1/create");
        return walletService.doCreateNew(req);
    }

    @PostMapping(value = "/inquiry", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public WalletRes doInquiry(@RequestBody WalletReq req, HttpServletRequest servletRequest) throws JsonProcessingException {
        log.info("/wallet/v1/inquiry");
        return walletService.doInquiry(req);
    }

    @PostMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public WalletRes getHistory(@RequestBody WalletReq req, HttpServletRequest servletRequest) throws JsonProcessingException {
        log.info("/wallet/v1/history");
        return walletService.getHistory(req);
    }

    @PostMapping(value = "/update/balance", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public WalletRes doUpdateBallance(@RequestBody WalletReq req, HttpServletRequest servletRequest) throws JsonProcessingException {
        log.info("/wallet/v1/update/balance");
        return walletService.doUpdateBalance(req);
    }

}
