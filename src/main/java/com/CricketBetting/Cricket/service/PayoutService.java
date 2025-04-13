package com.CricketBetting.Cricket.service;

import com.CricketBetting.Cricket.domain.response.PayoutResponseDto;
import com.CricketBetting.Cricket.domain.response.PayoutSummaryResponseDto;

import java.util.List;
import java.util.UUID;

public interface PayoutService {
    void processPayoutForMatch(UUID matchId);

    List<PayoutResponseDto> getPayoutsByMatchId(UUID matchId);

    List<PayoutResponseDto> getPayoutsByUserId(UUID userId);

    PayoutSummaryResponseDto getPayoutSummary(UUID matchId);
}