package com.ian.gamification.game.badgeprocessors;

import com.ian.gamification.game.domain.BadgeType;
import com.ian.gamification.game.domain.ScoreCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FirstWonBadgeProcessorTest {
    private FirstWonBadgeProcessor badgeProcessor;

    @BeforeEach
    public void setUp() {
        badgeProcessor = new FirstWonBadgeProcessor();
    }

    @Test
    public void should_give_badge_if_first_time() {
        var badgeType = badgeProcessor.processForOptionalBadge(10, List.of(new ScoreCard(1L, 1L)), null);
        assertThat(badgeType).contains(BadgeType.FIRST_WON);
    }

    @Test
    public void should_not_give_badge_if_not_first_time() {
        var badgeType = badgeProcessor.processForOptionalBadge(10,
                List.of(new ScoreCard(1L, 1L), new ScoreCard(1L, 2L)),
                null);
        assertThat(badgeType).isEmpty();
    }

}