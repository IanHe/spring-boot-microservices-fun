package com.ian.gamification.game.badgeprocessors;

import com.ian.gamification.game.domain.BadgeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SilverBadgeProcessorTest {

    private SilverBadgeProcessor badgeProcessor;

    @BeforeEach
    public void setUp() {
        badgeProcessor = new SilverBadgeProcessor();
    }

    @Test
    public void should_give_badge_if_score_over_threshold() {
        Optional<BadgeType> badgeType = badgeProcessor
                .processForOptionalBadge(160, List.of(), null);
        assertThat(badgeType).contains(BadgeType.SILVER);
    }

    @Test
    public void should_not_give_badge_if_score_under_threshold() {
        Optional<BadgeType> badgeType = badgeProcessor
                .processForOptionalBadge(140, List.of(), null);
        assertThat(badgeType).isEmpty();
    }
}