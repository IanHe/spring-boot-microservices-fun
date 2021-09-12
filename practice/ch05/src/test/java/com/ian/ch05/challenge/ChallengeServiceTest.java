package com.ian.ch05.challenge;

import com.ian.ch05.user.User;
import com.ian.ch05.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    private ChallengeService challengeService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ChallengeAttemptRepository attemptRepository;

    @BeforeEach
    public void setUp() {
        challengeService = new ChallengeServiceImpl(userRepository, attemptRepository);
    }

    @Test
    public void check_correct_attempt() {
        // given
        given(attemptRepository.save(any())).will(returnsFirstArg());
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 3000);

        // when
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        // then
        then(resultAttempt.isCorrect()).isTrue();
        verify(userRepository).save(new User("john_doe"));
        verify(attemptRepository).save(resultAttempt);
    }

    @Test
    public void check_wrong_attempt() {
        // given
        given(attemptRepository.save(any())).will(returnsFirstArg());
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);

        // when
        ChallengeAttempt result = challengeService.verifyAttempt(attemptDTO);

        // then
        then(result.isCorrect()).isFalse();
        verify(userRepository).save(new User("john_doe"));
        verify(attemptRepository).save(result);
    }

    @Test
    public void check_existing_user() {
        // given
        given(attemptRepository.save(any())).will(returnsFirstArg());
        var user = new User(1L, "john_doe");
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.of(user));
        var attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);

        // when
        var result = challengeService.verifyAttempt(attemptDTO);

        // then
        then(result.isCorrect()).isFalse();
        verify(userRepository, never()).save(any());
        verify(attemptRepository).save(result);
    }

    @Test
    public void retrieve_stats() {
        // given
        var user = new User(1L, "john_doe");
        ChallengeAttempt attempt1 = new ChallengeAttempt(1L, user, 50, 60, 3010, false);
        ChallengeAttempt attempt2 = new ChallengeAttempt(2L, user, 50, 60, 3051, false);
        var lastAttempts = List.of(attempt1, attempt2);
        given(attemptRepository.findTop10ByUserAliasOrderByIdDesc("john_doe")).willReturn(lastAttempts);

        // when
        var result = challengeService.getStatsForUser("john_doe");

        // then
        then(result).isEqualTo(lastAttempts);

    }
}