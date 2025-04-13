package com.CricketBetting.Cricket.controller;

import com.CricketBetting.Cricket.domain.enums.Team;
import com.CricketBetting.Cricket.domain.request.DeclareResultRequestDto;
import com.CricketBetting.Cricket.domain.request.MatchRequest;
import com.CricketBetting.Cricket.domain.request.PlaceBetRequest;
import com.CricketBetting.Cricket.domain.response.MatchResponse;
import com.CricketBetting.Cricket.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatchResponse> createMatch(@Valid @RequestBody MatchRequest request) {
        return ResponseEntity.ok(matchService.createMatch(request));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<MatchResponse>> getUpcomingMatches() {
        return ResponseEntity.ok(matchService.getUpcomingMatches());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<MatchResponse>> getCompletedMatches() {
        return ResponseEntity.ok(matchService.getCompletedMatches());
    }

    @GetMapping("/ongoing")
    public ResponseEntity<List<MatchResponse>> getOngoingMatches() {
        return ResponseEntity.ok(matchService.getOngoingMatches());
    }

    @PostMapping("/declareResult")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> declareMatchResult(@Valid @RequestBody DeclareResultRequestDto request) {
        matchService.declareMatchResult(request);
        return ResponseEntity.ok("Match result declared successfully");
    }

}
