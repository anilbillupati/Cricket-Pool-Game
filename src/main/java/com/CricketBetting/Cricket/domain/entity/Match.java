package com.CricketBetting.Cricket.domain.entity;

import com.CricketBetting.Cricket.domain.enums.MatchStatus;
import com.CricketBetting.Cricket.domain.enums.Team;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Team teamA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Team teamB;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status;

    @Enumerated(EnumType.STRING)
    private Team winner;

    @Column(nullable = false)
    private BigDecimal betAmount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = MatchStatus.UPCOMING;
    }
}
