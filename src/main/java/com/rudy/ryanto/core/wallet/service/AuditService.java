package com.rudy.ryanto.core.wallet.service;

import com.rudy.ryanto.core.wallet.domain.AuditData;
import com.rudy.ryanto.core.wallet.util.WalletConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public <T> void sendAudit(T auditData) {
        log.info("send audit data : {}", auditData);
        try {
            AuditData data = (AuditData) auditData;
            kafkaTemplate.send(WalletConstant.AUDIT_TOPIC, data);
            log.info("success send messaging audit !");
        } catch (Exception e) {
            log.error("error send audit messaging, caused : ", e);
            if (!kafkaTemplate.inTransaction()) {
                kafkaTemplate.destroy();
            }
        } finally {
            if (!kafkaTemplate.inTransaction()) {
                kafkaTemplate.destroy();
            }
        }
    }
}
