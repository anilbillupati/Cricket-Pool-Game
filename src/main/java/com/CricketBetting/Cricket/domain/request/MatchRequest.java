package com.CricketBetting.Cricket.domain.request;

import com.CricketBetting.Cricket.domain.enums.Team;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MatchRequest {
    @NotNull
    private Team teamA;

    @NotNull
    private Team teamB;

    @NotNull
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull
    private BigDecimal betAmount;
}