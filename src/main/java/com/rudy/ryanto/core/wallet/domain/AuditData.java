package com.rudy.ryanto.core.wallet.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuditData extends AuditTrail{
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
    private String optionalDetailsData2;
    private String stage;
    private String flowName;
    private String clientId;

}
