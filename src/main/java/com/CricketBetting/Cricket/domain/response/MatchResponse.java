package com.CricketBetting.Cricket.domain.response;

import com.CricketBetting.Cricket.domain.enums.MatchStatus;
import com.CricketBetting.Cricket.domain.enums.Team;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MatchResponse {
    private UUID id;
    private Team teamA;
    private Team teamB;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal betAmount;
    private MatchStatus status;
    private Team winner;
}
