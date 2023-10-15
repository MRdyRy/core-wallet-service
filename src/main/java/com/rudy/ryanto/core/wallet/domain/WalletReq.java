package com.rudy.ryanto.core.wallet.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletReq {
    private Long id;
    private String walletName;
    private String norek;
    private Long userId;
    private BigDecimal amount;
    private String clientId;
    private String type;
}
