package com.CricketBetting.Cricket.mapper;

import com.CricketBetting.Cricket.domain.entity.Match;

import com.CricketBetting.Cricket.domain.request.MatchRequest;
import com.CricketBetting.Cricket.domain.response.MatchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "UPCOMING")
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "winner", ignore = true)
    Match toEntity(MatchRequest request);

    MatchResponse toResponse(Match match);
}