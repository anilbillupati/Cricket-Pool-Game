package com.CricketBetting.Cricket.domain.entity;

import com.CricketBetting.Cricket.domain.enums.BetResult;
import com.CricketBetting.Cricket.domain.enums.Team;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Team teamChosen;

    @Enumerated(EnumType.STRING)
    private BetResult result;

    @Column(nullable = false)
    private BigDecimal amount;

    private LocalDateTime placedAt;

    @PrePersist
    public void prePersist() {
        this.placedAt = LocalDateTime.now();
    }
}
