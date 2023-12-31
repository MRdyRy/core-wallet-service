package com.rudy.ryanto.core.wallet.repository;

import com.rudy.ryanto.core.wallet.entity.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory,Long> {

    WalletHistory findByWalletId(Long walletId);
}
