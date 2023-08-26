package com.rudy.ryanto.core.wallet.controller;

import com.rudy.ryanto.core.wallet.domain.WalletReq;
import com.rudy.ryanto.core.wallet.domain.WalletRes;
import com.rudy.ryanto.core.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wallet/v1")
@Slf4j
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    @ResponseBody
    public WalletRes doCreate(@RequestBody WalletReq req, HttpServletRequest servletRequest){
        log.info("/wallet/v1/create");
        return walletService.doCreateNew(req,servletRequest);
    }

    @PostMapping("/inquiry")
    @ResponseBody
    public WalletRes doInquiry(@RequestBody WalletReq req, HttpServletRequest servletRequest){
        log.info("/wallet/v1/inquiry");
        return walletService.doInquiry(req,servletRequest);
    }

    @PostMapping("/history")
    @ResponseBody
    public WalletRes getHistory(@RequestBody WalletReq req, HttpServletRequest servletRequest){
        log.info("/wallet/v1/history");
        return walletService.getHistory(req,servletRequest);
    }

    @PostMapping("/update/balance")
    @ResponseBody
    public WalletRes doUpdateBallance(@RequestBody WalletReq req,HttpServletRequest servletRequest){
        log.info("/wallet/v1/update/balance");
        return walletService.doUpdateBalance(req,servletRequest);
    }

}
