package com.ian.gamification.game.domain;

import lombok.Value;

import java.util.List;

@Value
public class GameResult {
    int score;
    List<BadgeType> badges;
}
