package com.rudy.ryanto.core.wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "WALLET_HISTORY_DETAIL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletHistoryDetail {

    /**
     * KONSEP NYA JURNAL
     * MISAL ADA TRF DARI REK 123 KE 456;
     * THEN :
     * ID_MASTER |  NOREK   |   DEBET   |   CREDIT  |
     * 1        |   123     |   10.000  |   NULL    |
     * 2        |   456     | NULL      |   10.000  |
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID",nullable = false)
    private Long id;
    @Column(name = "ID_MASTER", nullable = false)
    private Long idMaster;
    @Column(name = "NOREK", nullable = false)
    private String norek;
    @Column(name = "DEBET", precision = 2)
    private BigDecimal debet;
    @Column(name = "CREDIT", precision = 2)
    private BigDecimal credit;
    @Column(name = "SEQUENCE")
    private int sequence;
}
