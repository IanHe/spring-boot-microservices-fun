package com.ian.gamification.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BadgeCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long badgeId;

    private Long userId;
    @EqualsAndHashCode.Exclude
    private long badgeTimestamp;
    private BadgeType badgeType;

    public BadgeCard(Long badgeId, BadgeType badgeType) {
        this.badgeId = badgeId;
        this.badgeType = badgeType;
    }
}
