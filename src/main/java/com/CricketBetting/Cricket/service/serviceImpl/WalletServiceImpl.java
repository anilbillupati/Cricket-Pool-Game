package com.CricketBetting.Cricket.service.serviceImpl;

import com.CricketBetting.Cricket.domain.entity.User;
import com.CricketBetting.Cricket.domain.entity.WalletTransaction;
import com.CricketBetting.Cricket.domain.enums.TransactionType;
import com.CricketBetting.Cricket.domain.request.AdminCreditRequestDto;
import com.CricketBetting.Cricket.domain.response.AdminCreditResponseDto;
import com.CricketBetting.Cricket.domain.response.WalletBalanceResponseDto;
import com.CricketBetting.Cricket.domain.response.WalletTransactionResponseDto;
import com.CricketBetting.Cricket.exceptions.ResourceNotFoundException;
import com.CricketBetting.Cricket.mapper.WalletTransactionMapper;
import com.CricketBetting.Cricket.repository.UserRepository;
import com.CricketBetting.Cricket.repository.WalletTransactionRepository;
import com.CricketBetting.Cricket.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionMapper transactionMapper;

    @Override
    public WalletBalanceResponseDto getWalletBalance(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new WalletBalanceResponseDto(user.getId(), user.getFullName(), user.getEmail(), user.getWalletBalance());
    }

    @Override
    public Page<WalletTransactionResponseDto> getWalletTransactions(UUID userId, int page, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return walletTransactionRepository.findByUserOrderByTimestampDesc(user, pageable).map(transactionMapper::toDto);
    }

    @Override
    public AdminCreditResponseDto creditWalletByAdmin(AdminCreditRequestDto requestDto) {
        if (requestDto.getAmount() == null || requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDto.getUserId()));

        user.setWalletBalance(user.getWalletBalance().add(requestDto.getAmount()));
        userRepository.save(user);

        WalletTransaction transaction = WalletTransaction.builder().user(user).amount(requestDto.getAmount())
                .type(TransactionType.ADMIN_CREDIT).reason(requestDto.getReason()).timestamp(LocalDateTime.now())
                .build();

        walletTransactionRepository.save(transaction);

        return new AdminCreditResponseDto(user.getId(), user.getFullName(), requestDto.getAmount(),
                "Wallet credited successfully");
    }
}
