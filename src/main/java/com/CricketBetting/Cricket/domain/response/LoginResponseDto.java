package com.CricketBetting.Cricket.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String email;
    private String role;
    private UUID userId;
}
