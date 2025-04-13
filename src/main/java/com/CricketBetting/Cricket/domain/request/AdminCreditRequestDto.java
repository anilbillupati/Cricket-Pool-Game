package com.CricketBetting.Cricket.domain.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AdminCreditRequestDto {
    private UUID userId;
    private BigDecimal amount;
    private String reason;
}
