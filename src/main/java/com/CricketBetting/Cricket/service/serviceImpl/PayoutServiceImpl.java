package com.CricketBetting.Cricket.service.serviceImpl;

import com.CricketBetting.Cricket.domain.entity.*;
import com.CricketBetting.Cricket.domain.enums.BetResult;
import com.CricketBetting.Cricket.domain.enums.MatchStatus;
import com.CricketBetting.Cricket.domain.enums.Team;
import com.CricketBetting.Cricket.domain.enums.TransactionType;
import com.CricketBetting.Cricket.domain.response.PayoutResponseDto;
import com.CricketBetting.Cricket.domain.response.PayoutSummaryResponseDto;
import com.CricketBetting.Cricket.exceptions.ResourceNotFoundException;
import com.CricketBetting.Cricket.mapper.PayoutMapper;
import com.CricketBetting.Cricket.repository.*;
import com.CricketBetting.Cricket.service.PayoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutServiceImpl implements PayoutService {

    private final MatchRepository matchRepository;
    private final BetRepository betRepository;
    private final PayoutRepository payoutRepository;
    private final UserRepository userRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final PayoutMapper payoutMapper;

    @Override
    @Transactional
    public void processPayoutForMatch(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        // ✅ Check if match is completed and winner exists
        if (match.getStatus() != MatchStatus.COMPLETED || match.getWinner() == null) {
            throw new IllegalStateException("Match is not completed or winner not declared yet.");
        }

        // ✅ Prevent duplicate payouts
        if (payoutRepository.existsByMatch(match)) {
            throw new IllegalStateException("Payout already processed for this match.");
        }

        // ✅ Get winning and losing bets
        List<Bet> winningBets = betRepository.findByMatchAndTeamChosen(match, match.getWinner());
        Team losingTeam = match.getWinner() == match.getTeamA() ? match.getTeamB() : match.getTeamA();
        List<Bet> losingBets = betRepository.findByMatchAndTeamChosen(match, losingTeam);

        if (winningBets.isEmpty()) {
            System.out.println("No winning bets found. No payout distributed.");
            return;
        }

        if (losingBets.isEmpty()) {
            System.out.println("No losing bets found. Winnings = 0.");
            return;
        }

        // ✅ Calculate total pool from losers and distribute to winners
        BigDecimal totalPool = match.getBetAmount().multiply(BigDecimal.valueOf(losingBets.size()));
        BigDecimal winningsPerUser = totalPool.divide(BigDecimal.valueOf(winningBets.size()), 2, RoundingMode.DOWN);

        for (Bet bet : winningBets) {
            User user = bet.getUser();

            // ✅ Credit wallet
            user.setWalletBalance(user.getWalletBalance().add(winningsPerUser));
            userRepository.save(user);

            // ✅ Record transaction
            WalletTransaction transaction = WalletTransaction.builder().user(user).match(match).amount(winningsPerUser)
                    .type(TransactionType.WIN_CREDIT).reason("Winnings for match " + match.getId())
                    .timestamp(java.time.LocalDateTime.now()).build();
            walletTransactionRepository.save(transaction);

            // ✅ Save payout
            Payout payout = Payout.builder().user(user).match(match).amount(winningsPerUser).build();
            payoutRepository.save(payout);
        }

        System.out.println("✅ Payout processed for match: " + matchId);
    }

    @Transactional
    protected void processSinglePayout(Bet bet, BigDecimal amount, Match match) {
        User user = bet.getUser();

        // 1. Update user wallet balance
        user.setWalletBalance(user.getWalletBalance().add(amount));
        userRepository.save(user);

        // 2. Create wallet transaction
        WalletTransaction transaction = WalletTransaction.builder().user(user).amount(amount)
                .type(TransactionType.WIN_CREDIT).match(match).timestamp(LocalDateTime.now()).build();
        walletTransactionRepository.save(transaction);

        // 3. Create payout record
        Payout payout = Payout.builder().user(user).match(match).amount(amount).creditedAt(LocalDateTime.now()).build();
        payoutRepository.save(payout);

        log.info("Processed payout for user: {}, match: {}, amount: {}", user.getId(), match.getId(), amount);
    }

    @Override
    public List<PayoutResponseDto> getPayoutsByMatchId(UUID matchId) {
        List<Payout> payouts = payoutRepository.findByMatchId(matchId);
        return payoutMapper.toDtoList(payouts);
    }

    @Override
    public List<PayoutResponseDto> getPayoutsByUserId(UUID userId) {
        List<Payout> payouts = payoutRepository.findByUserId(userId);
        return payoutMapper.toDtoList(payouts);
    }

    @Override
    public PayoutSummaryResponseDto getPayoutSummary(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (match.getStatus() != MatchStatus.COMPLETED || match.getWinner() == null) {
            throw new IllegalStateException("Match is not completed or winner is not declared");
        }

        List<Payout> payouts = payoutRepository.findByMatchId(matchId);

        if (payouts.isEmpty()) {
            throw new IllegalStateException("No payouts processed for this match");
        }

        // Get all bets for the match
        Team winningTeam = match.getWinner();
        List<Bet> winningBets = betRepository.findByMatchIdAndTeamChosen(matchId, winningTeam.name());
        List<Bet> losingBets = betRepository.findByMatchIdAndTeamChosenNot(matchId, winningTeam.name());

        BigDecimal totalLosingPool = BigDecimal.valueOf(losingBets.size()).multiply(match.getBetAmount());
        BigDecimal amountPerWinner = !winningBets.isEmpty()
                ? totalLosingPool.divide(BigDecimal.valueOf(winningBets.size()), 2, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

        return PayoutSummaryResponseDto.builder().matchId(match.getId()).teamA(match.getTeamA().name())
                .teamB(match.getTeamB().name()).winningTeam(winningTeam.name()).totalLosingPool(totalLosingPool)
                .totalWinners(winningBets.size()).amountPerWinner(amountPerWinner)
                .payouts(payoutMapper.toDtoList(payouts)).build();
    }
}