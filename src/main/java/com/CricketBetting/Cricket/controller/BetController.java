package com.CricketBetting.Cricket.controller;

import com.CricketBetting.Cricket.domain.request.PlaceBetRequest;
import com.CricketBetting.Cricket.domain.response.BetResponse;
import com.CricketBetting.Cricket.domain.response.WalletTransactionResponseDto;
import com.CricketBetting.Cricket.security.CurrentUser;
import com.CricketBetting.Cricket.service.BetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping("/place")
    public ResponseEntity<BetResponse> placeBet(@RequestBody @Valid PlaceBetRequest request,
            @AuthenticationPrincipal CurrentUser principal) {
        return ResponseEntity.ok(betService.placeBet(principal.getId(), request));
    }

    @GetMapping("/myBets")
    public ResponseEntity<List<BetResponse>> getMyBets(@AuthenticationPrincipal CurrentUser principal) {
        return ResponseEntity.ok(betService.getMyBets(principal.getId()));
    }

    @GetMapping("/walletTransactions")
    public ResponseEntity<List<WalletTransactionResponseDto>> getWalletTransactions(
            @AuthenticationPrincipal CurrentUser principal) {
        return ResponseEntity.ok(betService.getWalletTransactions(principal.getId()));
    }
}