package com.CricketBetting.Cricket.service;

import com.CricketBetting.Cricket.domain.request.AdminCreditRequestDto;
import com.CricketBetting.Cricket.domain.response.AdminCreditResponseDto;
import com.CricketBetting.Cricket.domain.response.WalletBalanceResponseDto;
import com.CricketBetting.Cricket.domain.response.WalletTransactionResponseDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface WalletService {
    WalletBalanceResponseDto getWalletBalance(UUID userId);

    Page<WalletTransactionResponseDto> getWalletTransactions(UUID userId, int page, int size);

    AdminCreditResponseDto creditWalletByAdmin(AdminCreditRequestDto requestDto);

}
