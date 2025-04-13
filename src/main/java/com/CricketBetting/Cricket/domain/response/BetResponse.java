package com.CricketBetting.Cricket.domain.response;

import com.CricketBetting.Cricket.domain.enums.BetResult;
import com.CricketBetting.Cricket.domain.enums.Team;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BetResponse {

    private UUID id;
    private UUID matchId;
    private UUID userId;
    private Team teamChosen;
    private BigDecimal amount;
    private LocalDateTime placedAt;
    private BetResult result;
}
