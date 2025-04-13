package com.CricketBetting.Cricket.service.serviceImpl;

import com.CricketBetting.Cricket.domain.entity.Bet;
import com.CricketBetting.Cricket.domain.entity.Match;
import com.CricketBetting.Cricket.domain.entity.User;
import com.CricketBetting.Cricket.domain.entity.WalletTransaction;
import com.CricketBetting.Cricket.domain.enums.MatchStatus;
import com.CricketBetting.Cricket.domain.enums.TransactionType;
import com.CricketBetting.Cricket.domain.request.PlaceBetRequest;
import com.CricketBetting.Cricket.domain.response.BetResponse;
import com.CricketBetting.Cricket.domain.response.WalletTransactionResponseDto;
import com.CricketBetting.Cricket.exceptions.ResourceNotFoundException;
import com.CricketBetting.Cricket.mapper.BetMapper;
import com.CricketBetting.Cricket.mapper.WalletTransactionMapper;
import com.CricketBetting.Cricket.repository.BetRepository;
import com.CricketBetting.Cricket.repository.MatchRepository;
import com.CricketBetting.Cricket.repository.UserRepository;
import com.CricketBetting.Cricket.repository.WalletTransactionRepository;
import com.CricketBetting.Cricket.service.BetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final BetRepository betRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final BetMapper betMapper;
    private final WalletTransactionMapper walletTransactionMapper;

    @Override
    @Transactional
    public BetResponse placeBet(UUID userId, PlaceBetRequest request) {
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (!match.getStatus().equals(MatchStatus.UPCOMING) || match.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Betting is closed for this match.");
        }

        if (betRepository.existsByUserIdAndMatchId(userId, request.getMatchId())) {
            throw new IllegalStateException("You’ve already placed a bet for this match.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BigDecimal betAmount = match.getBetAmount();
        if (user.getWalletBalance().compareTo(betAmount) < 0) {
            throw new IllegalStateException("Insufficient wallet balance");
        }

        user.setWalletBalance(user.getWalletBalance().subtract(betAmount));

        Bet bet = new Bet();
        bet.setMatch(match);
        bet.setUser(user);
        bet.setTeamChosen(request.getTeamChosen());
        bet.setAmount(betAmount);
        bet.setPlacedAt(LocalDateTime.now());

        WalletTransaction transaction = new WalletTransaction();
        transaction.setUser(user);
        transaction.setAmount(betAmount.negate());
        transaction.setType(TransactionType.BET_PLACED);
        transaction.setMatch(match);
        transaction.setTimestamp(LocalDateTime.now());

        betRepository.save(bet);
        walletTransactionRepository.save(transaction);
        userRepository.save(user);

        return betMapper.toResponse(bet);
    }

    @Override
    public List<BetResponse> getMyBets(UUID userId) {
        List<Bet> bets = betRepository.findAllByUserIdOrderByPlacedAtDesc(userId);
        return betMapper.toResponseList(bets);
    }

    @Override
    public List<WalletTransactionResponseDto> getWalletTransactions(UUID userId) {
        List<WalletTransaction> walletTransactions = walletTransactionRepository
                .findByUserIdOrderByTimestampDesc(userId);
        return walletTransactionMapper.toResponseList(walletTransactions);
    }

}
