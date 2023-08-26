package com.rudy.ryanto.core.wallet.repository;

import com.rudy.ryanto.core.wallet.entity.TblSeqRek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeqRekRepository extends JpaRepository<TblSeqRek,String> {
}
