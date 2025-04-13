package com.CricketBetting.Cricket.repository;

import com.CricketBetting.Cricket.domain.entity.Match;
import com.CricketBetting.Cricket.domain.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {
    List<Match> findByStatusAndStartTimeAfterOrderByStartTimeAsc(MatchStatus status, LocalDateTime now);

    List<Match> findByStatusAndStartTimeLessThanEqual(MatchStatus status, LocalDateTime time);

    List<Match> findByStatus(MatchStatus status);

}