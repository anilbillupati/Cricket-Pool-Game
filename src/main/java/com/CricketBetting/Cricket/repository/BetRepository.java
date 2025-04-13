package com.CricketBetting.Cricket.repository;

import com.CricketBetting.Cricket.domain.entity.Bet;
import com.CricketBetting.Cricket.domain.entity.Match;
import com.CricketBetting.Cricket.domain.enums.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BetRepository extends JpaRepository<Bet, UUID> {
    boolean existsByUserIdAndMatchId(UUID userId, UUID matchId);

    List<Bet> findAllByUserIdOrderByPlacedAtDesc(UUID userId);

    List<Bet> findByMatchIdAndTeamChosen(UUID matchId, String teamChosen);

    List<Bet> findByMatchIdAndTeamChosenNot(UUID matchId, String teamChosen);

    List<Bet> findByMatchAndTeamChosen(Match match, Team teamChosen);

    List<Bet> findByMatchAndTeamChosenNot(Match match, Team teamNotChosen);

}