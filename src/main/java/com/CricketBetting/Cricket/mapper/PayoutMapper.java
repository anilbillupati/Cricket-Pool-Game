package com.CricketBetting.Cricket.mapper;

import com.CricketBetting.Cricket.domain.entity.Payout;
import com.CricketBetting.Cricket.domain.response.PayoutResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PayoutMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userName")
    @Mapping(source = "match.id", target = "matchId")
    PayoutResponseDto toDto(Payout payout);

    List<PayoutResponseDto> toDtoList(List<Payout> payouts);
}