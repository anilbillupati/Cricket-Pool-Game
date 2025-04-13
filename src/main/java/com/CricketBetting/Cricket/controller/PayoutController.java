package com.CricketBetting.Cricket.controller;

import com.CricketBetting.Cricket.domain.response.PayoutResponseDto;
import com.CricketBetting.Cricket.domain.response.PayoutSummaryResponseDto;
import com.CricketBetting.Cricket.security.CurrentUser;
import com.CricketBetting.Cricket.service.PayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payouts")
@RequiredArgsConstructor
public class PayoutController {

    private final PayoutService payoutService;

    @PostMapping("/process/{matchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> processPayout(@PathVariable UUID matchId) {
        payoutService.processPayoutForMatch(matchId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<PayoutResponseDto>> getPayoutsByMatch(@PathVariable UUID matchId) {
        return ResponseEntity.ok(payoutService.getPayoutsByMatchId(matchId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PayoutResponseDto>> getMyPayouts(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(payoutService.getPayoutsByUserId(user.getId()));
    }

    @GetMapping("/summary/{matchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PayoutSummaryResponseDto> getPayoutSummary(@PathVariable UUID matchId) {
        return ResponseEntity.ok(payoutService.getPayoutSummary(matchId));
    }
}