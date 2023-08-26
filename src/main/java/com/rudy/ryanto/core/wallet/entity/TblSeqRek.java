package com.rudy.ryanto.core.wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "TBL_SEQ_REK")
public class TblSeqRek {
    private String key;
    private Integer seq;
}
