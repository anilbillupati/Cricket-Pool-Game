package com.CricketBetting.Cricket.service.serviceImpl;

import com.CricketBetting.Cricket.domain.entity.Match;
import com.CricketBetting.Cricket.domain.enums.MatchStatus;
import com.CricketBetting.Cricket.domain.enums.Team;
import com.CricketBetting.Cricket.domain.request.DeclareResultRequestDto;
import com.CricketBetting.Cricket.domain.request.MatchRequest;
import com.CricketBetting.Cricket.domain.request.PlaceBetRequest;
import com.CricketBetting.Cricket.domain.response.MatchResponse;
import com.CricketBetting.Cricket.exceptions.ResourceNotFoundException;
import com.CricketBetting.Cricket.mapper.MatchMapper;
import com.CricketBetting.Cricket.repository.MatchRepository;
import com.CricketBetting.Cricket.service.MatchService;
import com.CricketBetting.Cricket.service.PayoutService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final PayoutService payoutService;

    @Override
    public MatchResponse createMatch(MatchRequest request) {
        if (request.getTeamA().equals(request.getTeamB())) {
            throw new IllegalArgumentException("Team A and Team B must be different");
        }

        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Match start time must be in the future");
        }

        Match match = matchMapper.toEntity(request);
        match.setCreatedAt(LocalDateTime.now());
        matchRepository.save(match);
        return matchMapper.toResponse(match);
    }

    @Override
    public List<MatchResponse> getUpcomingMatches() {
        return matchRepository
                .findByStatusAndStartTimeAfterOrderByStartTimeAsc(MatchStatus.UPCOMING, LocalDateTime.now()).stream()
                .map(matchMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MatchResponse> getOngoingMatches() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Fetch all matches that are UPCOMING but their startTime <= now
        List<Match> toUpdate = matchRepository.findByStatusAndStartTimeLessThanEqual(MatchStatus.UPCOMING, now);
        for (Match match : toUpdate) {
            match.setStatus(MatchStatus.ONGOING);
        }
        matchRepository.saveAll(toUpdate); // persist the status changes

        // 2. Fetch and return all ONGOING matches
        return matchRepository.findByStatus(MatchStatus.ONGOING).stream()
                .sorted(Comparator.comparing(Match::getStartTime).reversed()).map(matchMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchResponse> getCompletedMatches() {
        return matchRepository.findByStatus(MatchStatus.COMPLETED).stream().map(matchMapper::toResponse)
                .collect(Collectors.toList());
    }

    // @Override
    // @Transactional
    // public MatchResponse declareResult(PlaceBetRequest request) {
    // Match match = matchRepository.findById(request.getMatchId())
    // .orElseThrow(() -> new EntityNotFoundException("Match not found"));
    //
    // if (match.getWinner() != null || match.getStatus() == MatchStatus.COMPLETED) {
    // throw new IllegalStateException("Result already declared");
    // }
    //
    // if (match.getStartTime().isAfter(LocalDateTime.now())) {
    // throw new IllegalStateException("Cannot declare result before match start time");
    // }
    //
    // Team winningTeam = request.getTeamChosen();
    // if (winningTeam == null) {
    // throw new IllegalArgumentException("Winning team must be provided");
    // }
    //
    // // Check if the winning team is actually one of the teams in the match
    // String winningTeamName = winningTeam.name();
    // if (!winningTeamName.equals(match.getTeamA().name()) && !winningTeamName.equals(match.getTeamB().name())) {
    // throw new IllegalArgumentException("Invalid winning team for this match");
    // }
    //
    // match.setWinner(winningTeam);
    // match.setStatus(MatchStatus.COMPLETED);
    // match.setEndTime(LocalDateTime.now());
    //
    // matchRepository.save(match);
    // payoutService.processPayout(match.getId());
    //
    // return matchMapper.toResponse(match);
    // }

    @Override
    @Transactional
    public void declareMatchResult(DeclareResultRequestDto request) {
        UUID matchId = request.getMatchId();
        Team winner = request.getWinningTeam();

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        // Validate: already declared
        if (match.getWinner() != null || match.getStatus() == MatchStatus.COMPLETED) {
            throw new IllegalStateException("Result already declared for this match");
        }

        // Validate: match must have started
        if (match.getStartTime().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot declare result before match start time");
        }

        // Save winner and mark completed
        match.setWinner(winner);
        match.setStatus(MatchStatus.COMPLETED);
        match.setEndTime(LocalDateTime.now()); // optional but useful
        matchRepository.save(match);

        // Trigger payout logic
        payoutService.processPayoutForMatch(match.getId());
    }

}