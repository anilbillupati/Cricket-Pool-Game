package com.CricketBetting.Cricket.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AdminCreditResponseDto {
    private UUID userId;
    private String fullName;
    private BigDecimal amount;
    private String message;
}