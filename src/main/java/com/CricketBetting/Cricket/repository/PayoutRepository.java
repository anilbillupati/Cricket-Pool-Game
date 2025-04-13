package com.CricketBetting.Cricket.repository;

import com.CricketBetting.Cricket.domain.entity.Match;
import com.CricketBetting.Cricket.domain.entity.Payout;
import com.CricketBetting.Cricket.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayoutRepository extends JpaRepository<Payout, UUID> {
    boolean existsByMatchId(UUID matchId);

    boolean existsByMatch(Match match);

    List<Payout> findByMatchId(UUID matchId);

    List<Payout> findByUserId(UUID userId);

    List<Payout> findByMatch(Match match);

    Optional<Payout> findByUserAndMatch(User user, Match match);
}
