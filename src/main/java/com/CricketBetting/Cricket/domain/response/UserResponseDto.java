package com.CricketBetting.Cricket.domain.response;

import com.CricketBetting.Cricket.domain.enums.Role;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private String id;
    private String email;
    private String fullName;
    private Role role;
    private BigDecimal walletBalance;
    private LocalDateTime createdAt;
    private Boolean isActive;
}