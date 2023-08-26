package com.rudy.ryanto.core.wallet.repository;

import com.rudy.ryanto.core.wallet.entity.WalletHistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryDetailRepository extends JpaRepository<WalletHistoryDetail,Long> {

    WalletHistoryDetail findByIdMaster(Long idMaster);
}
