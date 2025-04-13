package com.CricketBetting.Cricket.service;

import com.CricketBetting.Cricket.domain.request.UserRequestDto;
import com.CricketBetting.Cricket.domain.response.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getUserListOnly(int page, int size);

    UserResponseDto getUserById(UUID id);

}