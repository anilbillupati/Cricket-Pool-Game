package com.CricketBetting.Cricket.domain.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayoutResponseDto {
    private UUID id;
    private UUID userId;
    private String userName;
    private UUID matchId;
    private BigDecimal amount;
    private LocalDateTime creditedAt;
}