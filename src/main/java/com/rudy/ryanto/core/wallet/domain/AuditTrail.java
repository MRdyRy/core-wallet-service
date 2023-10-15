package com.rudy.ryanto.core.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuditTrail implements Serializable {
    private String createBy;
    private String updateBy;
    private LocalDateTime createDate;
    private LocalDateTime UpdateDate;
}
