package com.rudy.ryanto.core.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditTrail implements Serializable {
    private String createBy;
    private String updateBy;
    private LocalDateTime createDate;
    private LocalDateTime UpdateDate;
}
