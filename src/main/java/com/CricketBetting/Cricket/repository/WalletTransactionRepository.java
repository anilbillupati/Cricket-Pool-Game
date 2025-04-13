package com.CricketBetting.Cricket.repository;

import com.CricketBetting.Cricket.domain.entity.User;
import com.CricketBetting.Cricket.domain.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    Page<WalletTransaction> findByUserOrderByTimestampDesc(User user, Pageable pageable);

    List<WalletTransaction> findByUserIdOrderByTimestampDesc(UUID userId);
}
