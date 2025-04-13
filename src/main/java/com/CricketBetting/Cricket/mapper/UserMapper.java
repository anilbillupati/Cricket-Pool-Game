package com.CricketBetting.Cricket.mapper;

import com.CricketBetting.Cricket.domain.entity.User;
import com.CricketBetting.Cricket.domain.request.UserRequestDto;
import com.CricketBetting.Cricket.domain.response.UserResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "walletBalance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    User toEntity(UserRequestDto dto);

    UserResponseDto toDto(User user);
}
