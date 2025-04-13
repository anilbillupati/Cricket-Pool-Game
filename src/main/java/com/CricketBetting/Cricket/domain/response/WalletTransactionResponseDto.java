package com.CricketBetting.Cricket.domain.response;

import com.CricketBetting.Cricket.domain.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WalletTransactionResponseDto {
    private BigDecimal amount;
    private TransactionType type;
    private UUID matchId;
    private String reason;
    private LocalDateTime timestamp;
}
