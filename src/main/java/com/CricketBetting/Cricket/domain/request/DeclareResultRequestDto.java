package com.CricketBetting.Cricket.domain.request;

import com.CricketBetting.Cricket.domain.enums.Team;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DeclareResultRequestDto {
    @NotNull
    private UUID matchId;
    @NotNull
    private Team winningTeam;

}