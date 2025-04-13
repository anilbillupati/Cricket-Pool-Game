package com.CricketBetting.Cricket.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Team {
    TeamA, TeamB;



    @JsonCreator
    public static Team fromString(String value) {
        for (Team team : Team.values()) {
            if (team.name().equalsIgnoreCase(value)) {
                return team;
            }
        }
        throw new IllegalArgumentException("Invalid team: " + value);
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
