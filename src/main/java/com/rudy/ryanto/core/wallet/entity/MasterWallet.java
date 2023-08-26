package com.rudy.ryanto.core.wallet.entity;

import com.rudy.ryanto.core.wallet.util.WalletConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "MASTER_WALLET")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MasterWallet {

    /**
     * DISINI MENCATAT SALDO
     */

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID",nullable = false)
    private Long id;
    @Column(name = "WALLET_NAME", nullable = false)
    private String walletName;
    @Column(name = "NOREK", nullable = false)
    private String norek;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "CURRENCY_CODE", nullable = false)
    private String currencyCode;
    @Column(name = "SALDO", nullable = false, precision = 2)
    private BigDecimal saldo;
    @Enumerated(EnumType.ORDINAL)
    private WalletConstant.STATUS status;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    private String createBy;
    private String updateBy;


}
