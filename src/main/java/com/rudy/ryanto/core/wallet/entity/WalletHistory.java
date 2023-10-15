package com.rudy.ryanto.core.wallet.entity;

import com.rudy.ryanto.core.wallet.util.WalletConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "WALLET_HISTORY")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletHistory {

    /**
     * DISINI CATAT HISTORY DARI WALLET MASTER
     * ID   |   WALLET_ID   |   DESCRIPTION     | STATUS    |
     * 1    |   123         |   TRANSFER TO 456 |   SUCCESS |
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID",nullable = false)
    private Long id;
    @Column(name = "WALLET_ID",nullable = false)
    private Long walletId;
    @Column(name = "DESCRIPTION",nullable = false)
    private String description;
    @Column(name = "STATUS", nullable = false)
    private WalletConstant.HISTORY_STATUS status;
    private Date createDate;
    private Date updateDate;
    private String createBy;
    private String updateBy;
}
