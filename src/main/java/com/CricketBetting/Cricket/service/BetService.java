package com.CricketBetting.Cricket.service;

import com.CricketBetting.Cricket.domain.request.PlaceBetRequest;
import com.CricketBetting.Cricket.domain.response.BetResponse;
import com.CricketBetting.Cricket.domain.response.WalletTransactionResponseDto;

import java.util.List;
import java.util.UUID;

public interface BetService {
    BetResponse placeBet(UUID userId, PlaceBetRequest request);

    List<BetResponse> getMyBets(UUID userId);

    List<WalletTransactionResponseDto> getWalletTransactions(UUID userId);
}
