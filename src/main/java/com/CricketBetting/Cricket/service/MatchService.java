package com.CricketBetting.Cricket.service;

import com.CricketBetting.Cricket.domain.enums.Team;
import com.CricketBetting.Cricket.domain.request.DeclareResultRequestDto;
import com.CricketBetting.Cricket.domain.request.MatchRequest;
import com.CricketBetting.Cricket.domain.request.PlaceBetRequest;
import com.CricketBetting.Cricket.domain.response.MatchResponse;

import java.util.List;
import java.util.UUID;

public interface MatchService {
    MatchResponse createMatch(MatchRequest request);

    List<MatchResponse> getUpcomingMatches();

    List<MatchResponse> getOngoingMatches();

    List<MatchResponse> getCompletedMatches();

    // MatchResponse declareResult(PlaceBetRequest request);

    void declareMatchResult(DeclareResultRequestDto request);

}