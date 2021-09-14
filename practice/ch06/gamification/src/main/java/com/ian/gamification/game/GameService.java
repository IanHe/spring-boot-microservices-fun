package com.ian.gamification.game;

import com.ian.gamification.challenge.ChallengeSolvedDTO;
import com.ian.gamification.game.domain.GameResult;

public interface GameService {
    /**
     * Process a new attempt from a given user.
     *
     * @param challenge the challenge data with user details, factors, etc.
     * @return a {@link GameResult} object containing the new score and badge cards obtained
     */
    GameResult newAttemptForUser(ChallengeSolvedDTO challenge);
}
