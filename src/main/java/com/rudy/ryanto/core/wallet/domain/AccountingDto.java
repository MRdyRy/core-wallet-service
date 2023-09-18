package com.rudy.ryanto.core.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingDto {
    private String norek;
    private BigDecimal amount;
    private char type;
    private Long idMaster;
}
