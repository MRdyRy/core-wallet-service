package com.rudy.ryanto.core.wallet.services;

import com.rudy.ryanto.core.wallet.domain.AuditData;
import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.service.AuditService;
import com.rudy.ryanto.core.wallet.util.WalletConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AuditService.class})
@TestPropertySource(locations = "classpath:application.properties")
public class AuditServiceTest {

    @MockBean
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private AuditService auditService;

    @Test
    public void sendAuditSuccess(){
        Mockito.when(kafkaTemplate.send(WalletConstant.AUDIT_TOPIC,auditData()))
                .thenReturn(new SettableListenableFuture<>());
        auditService.sendAudit(auditData());

    }

    @Test
    public void sendAuditFailed(){
        SettableListenableFuture<SendResult<String,Object>> future = new SettableListenableFuture<>();
        future.setException(new RuntimeException());
        Mockito.when(kafkaTemplate.send(WalletConstant.AUDIT_TOPIC,auditData()))
                .thenReturn(future);
        auditService.sendAudit(auditData());
    }


    @Test
    public void sendAuditThrowException(){
        Mockito.doThrow(new CoreWalletException("")).when(kafkaTemplate).send(any(),any());
        auditService.sendAudit(null);
    }

    private AuditData auditData(){
        return AuditData.builder()
                .createBy("SYSTEM")
                .createDate(LocalDateTime.now())
                .amount("10000")
                .stage(WalletConstant.STAGES.INQUIRY.name())
                .clientId("1123")
                .optionalDetailsData2("")
                .optionalDetailsData("")
                .flowName(WalletConstant.FLOW_WALLET.INQUIRY.getCode())
                .currency(WalletConstant.CURRENCY_CODE.IDR.getCode())
                .rekPenerima("2023001")
                .rekPengirim("2023002")
                .transactionType(WalletConstant.ACTIVITY.GET_HISTORY.name())
                .transactionId("W202300001")
                .transactionDateTime(new Date())
                .build();
    }
}
