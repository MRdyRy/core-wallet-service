package com.rudy.ryanto.core.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditData {
    private Long id;
    private String transactionId;
    private String transactionType;
    private String amount;
    private String currency;
    private String userId;
    private String userName;
    private String rekPenerima;
    private String rekPengirim;
    private Date transactionDateTime;

    private String optionalDetailsData;
}
