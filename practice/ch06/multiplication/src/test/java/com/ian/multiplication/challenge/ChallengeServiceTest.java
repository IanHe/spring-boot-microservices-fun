package com.ian.multiplication.challenge;

import com.ian.multiplication.serviceclients.GamificationServiceClient;
import com.ian.multiplication.user.User;
import com.ian.multiplication.user.UserRepository;
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
    @Mock
    private GamificationServiceClient gameClient;

    @BeforeEach
    public void setUp() {
        challengeService = new ChallengeServiceImpl(userRepository, attemptRepository, gameClient);
    }

    @Test
    public void check_correct_attempt_Test() {
        // given
        given(attemptRepository.save(any())).will(returnsFirstArg());
        var attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 3000);

        // when
        var result = challengeService.verifyAttempt(attemptDTO);

        // then
        then(result.isCorrect()).isTrue();
        verify(userRepository).save(new User("john_doe"));
        verify(attemptRepository).save(result);
        verify(gameClient).sendAttempt(result);
    }

    @Test
    public void check_wrong_attempt() {
        // given
        given(attemptRepository.save(any())).will(returnsFirstArg());
        var attemptDto = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);

        // when
        var result = challengeService.verifyAttempt(attemptDto);

        // then
        then(result.isCorrect()).isFalse();
        verify(userRepository).save(new User("john_doe"));
        verify(attemptRepository).save(result);
        verify(gameClient).sendAttempt(result);
    }

    @Test
    public void check_existing_user() {
        // given
        given(attemptRepository.save(any())).will(returnsFirstArg());
        var existingUser = new User(1L, "john_doe");
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.of(existingUser));
        var attemptDto = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);

        // when
        var result = challengeService.verifyAttempt(attemptDto);

        // then
        then(result.isCorrect()).isFalse();
        then(result.getUser()).isEqualTo(existingUser);
        verify(userRepository, never()).save(any());
        verify(gameClient).sendAttempt(result);
    }

    @Test
    public void retrieve_stats() {
        // given
        User user = new User("john_doe");
        ChallengeAttempt attempt1 = new ChallengeAttempt(1L, user, 50, 60, 3010, false);
        ChallengeAttempt attempt2 = new ChallengeAttempt(2L, user, 50, 60, 3051, false);
        List<ChallengeAttempt> lastAttempts = List.of(attempt1, attempt2);
        given(attemptRepository.findTop10ByUserAliasOrderByIdDesc("john_doe"))
                .willReturn(lastAttempts);

        // when
        List<ChallengeAttempt> latestAttemptsResult =
                challengeService.getStatsForUser("john_doe");

        // then
        then(latestAttemptsResult).isEqualTo(lastAttempts);
    }
}