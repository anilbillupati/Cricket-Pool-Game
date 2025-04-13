package com.CricketBetting.Cricket.mapper;

import com.CricketBetting.Cricket.domain.entity.Bet;
import com.CricketBetting.Cricket.domain.response.BetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BetMapper {
    @Mappings({ @Mapping(source = "match.id", target = "matchId"), @Mapping(source = "user.id", target = "userId") })
    BetResponse toResponse(Bet bet);

    List<BetResponse> toResponseList(List<Bet> bets);
}
