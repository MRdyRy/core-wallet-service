package com.rudy.ryanto.core.wallet.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rudy.ryanto.core.wallet.entity.MasterWallet;
import com.rudy.ryanto.core.wallet.entity.WalletHistory;
import com.rudy.ryanto.core.wallet.entity.WalletHistoryDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletRes {
    private MasterWallet masterWallet;
    private WalletHistory walletHistory;
    private WalletHistoryDetail walletHistoryDetail;
}
