package com.CricketBetting.Cricket.domain.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayoutSummaryResponseDto {
    private UUID matchId;
    private String teamA;
    private String teamB;
    private String winningTeam;
    private BigDecimal totalLosingPool;
    private int totalWinners;
    private BigDecimal amountPerWinner;
    private List<PayoutResponseDto> payouts;
}