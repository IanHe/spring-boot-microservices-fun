package com.ian.gamification.game;

import com.ian.gamification.game.domain.LeaderBoardRow;
import com.ian.gamification.game.domain.ScoreCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends CrudRepository<ScoreCard, Long> {

    /**
     * Gets the total score for a given user: the sum of the scores of all
     * their ScoreCards.
     *
     * @param userId the id of the user
     * @return the total score for the user, empty if the user doesn't exist
     */
    @Query("select sum(s.score) from ScoreCard s where s.userId = :userId group by s.userId")
    Optional<Integer> getTotalScoreForUser(@Param("userId") Long userId);

    /**
     * Retrieves a list of {@link LeaderBoardRow}s representing the Leader Board
     * of users and their total score.
     *
     * @return the leader board, sorted by highest score first.
     */
    @Query("select new com.ian.gamification.game.domain.LeaderBoardRow(s.userId, sum(s.score)) " +
            "from ScoreCard s group by s.userId order by sum(s.score) desc ")
    List<LeaderBoardRow> findFirst10();

    /**
     * Retrieves all the ScoreCards for a given user, identified by his user id.
     *
     * @param userId the id of the user
     * @return a list containing all the ScoreCards for the given user,
     * sorted by most recent.
     */
    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);
}
