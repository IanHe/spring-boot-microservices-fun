package com.ian.ch03.challenge;

import com.ian.ch03.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(ChallengeAttemptController.class)
class ChallengeAttemptControllerTest {
    @MockBean
    private ChallengeService challengeService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ChallengeAttemptDTO> jsonRequestAttempt;

    @Autowired
    private JacksonTester<ChallengeAttempt> jsonResultAttempt;

    @Test
    void post_valid_result() throws Exception {
        // given
        var user = new User(1L, "john");
        long attemptId = 5L;
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 70, "john", 3500);
        ChallengeAttempt expectedResponse = new ChallengeAttempt(attemptId, user, 50, 70, 3500, true);
        given(challengeService.verifyAttempt(eq(attemptDTO))).willReturn(expectedResponse);

        // when
        var requestBody = jsonRequestAttempt.write(attemptDTO).getJson();
        System.out.println("requestBody:");
        System.out.println(requestBody);
        MockHttpServletResponse response = mvc.perform(
                        post("/attempts").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andReturn().getResponse();

        // then
        var expectedResponseBody = jsonResultAttempt.write(expectedResponse).getJson();
        System.out.println("expectedResponseBody:");
        System.out.println(expectedResponseBody);
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(expectedResponseBody);
    }

    @Test
    void post_invalid_result() throws Exception {
        // given an attempt with invalid input data
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(2000, -70, "john", 1);

        // when
        var requestBody = jsonRequestAttempt.write(attemptDTO).getJson();
        var resp = mvc
                .perform(post("/attempts").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn()
                .getResponse();

        // then
        then(resp.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}