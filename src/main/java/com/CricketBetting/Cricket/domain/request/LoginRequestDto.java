package com.CricketBetting.Cricket.domain.request;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
