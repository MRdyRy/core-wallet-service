package com.rudy.ryanto.core.wallet.repository;

import com.rudy.ryanto.core.wallet.entity.MasterWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletMasterRepository extends JpaRepository<MasterWallet, Long> {

    MasterWallet findByUserId(Long userId);
}
