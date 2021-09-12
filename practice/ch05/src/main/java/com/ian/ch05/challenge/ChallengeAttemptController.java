package com.ian.ch05.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * This class provides a REST API to POST the attempts from users.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/attempts")
class ChallengeAttemptController {

    private final ChallengeService challengeService;

    @PostMapping
    ResponseEntity<ChallengeAttempt> postResult(
            @RequestBody @Valid ChallengeAttemptDTO challengeAttemptDTO) {
        var resp = challengeService.verifyAttempt(challengeAttemptDTO);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    ResponseEntity<ChallengeAttempt> getResult(@PathVariable Long id) {
        var attempt = challengeService.findChallenge(id);
        return ResponseEntity.ok(attempt);
    }

    @GetMapping
    ResponseEntity<List<ChallengeAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(
                challengeService.getStatsForUser(alias)
        );
    }
}