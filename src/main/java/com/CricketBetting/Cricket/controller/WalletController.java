package com.CricketBetting.Cricket.controller;

import com.CricketBetting.Cricket.domain.request.AdminCreditRequestDto;
import com.CricketBetting.Cricket.domain.response.AdminCreditResponseDto;
import com.CricketBetting.Cricket.domain.response.WalletBalanceResponseDto;
import com.CricketBetting.Cricket.domain.response.WalletTransactionResponseDto;
import com.CricketBetting.Cricket.security.CurrentUser;
import com.CricketBetting.Cricket.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<WalletBalanceResponseDto> getBalance(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(walletService.getWalletBalance(user.getId()));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<WalletTransactionResponseDto>> getTransactions(@AuthenticationPrincipal CurrentUser user,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(walletService.getWalletTransactions(user.getId(), page, size).getContent());
    }

    @PostMapping("/admin/credit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminCreditResponseDto> creditMoneyByAdmin(
            @RequestBody AdminCreditRequestDto adminCreditRequestDto) {
        AdminCreditResponseDto response = walletService.creditWalletByAdmin(adminCreditRequestDto);
        return ResponseEntity.ok(response);
    }
}
