package com.ian.gamification.game;

import com.ian.gamification.game.domain.BadgeCard;
import com.ian.gamification.game.domain.BadgeType;
import com.ian.gamification.game.domain.LeaderBoardRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LeaderBoardServiceImplTest {

    private LeaderBoardService leaderBoardService;

    @Mock
    private ScoreRepository scoreRepository;
    @Mock
    private BadgeRepository badgeRepository;

    @BeforeEach
    public void setUp() {
        leaderBoardService = new LeaderBoardServiceImpl(scoreRepository, badgeRepository);
    }

    @Test
    public void retrieve_leader_board_test() {
        // given
        var scoreRow = new LeaderBoardRow(1L, 300L, List.of());
        given(scoreRepository.findFirst10()).willReturn(List.of(scoreRow));
        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(1L))
                .willReturn(List.of(new BadgeCard(1L, BadgeType.LUCKY_NUMBER)));

        // when
        var leaderBoard = leaderBoardService.getCurrentLeaderBoard();

        // then
        var expectedLeaderBoard = List.of(
                new LeaderBoardRow(1L, 300L, List.of(BadgeType.LUCKY_NUMBER.getDescription()))
        );
        then(leaderBoard).isEqualTo(expectedLeaderBoard);
    }

}