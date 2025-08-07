package blog.jungmini.me.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.servlet.ResultActions;

import blog.jungmini.me.AbstractTestContainerTest;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.repository.FollowRepository;
import blog.jungmini.me.database.repository.UserRepository;
import blog.jungmini.me.dto.response.CreateFollowResponse;
import blog.jungmini.me.dto.response.CreateUserResponse;
import blog.jungmini.me.util.AuthUtil;

public class FollowController extends AbstractTestContainerTest {
    AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil(restTemplate, port);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @AfterEach
    void tearDown() {
        followRepository.deleteAll();
        userRepository.deleteAll();
    }

    UserEntity user1 = UserEntity.builder()
            .email("user1@test.com")
            .nickname("test")
            .password("qwer12345")
            .build();

    UserEntity user2 = UserEntity.builder()
            .email("user2@test.com")
            .nickname("test")
            .password("qwer12345")
            .build();

    @Test
    @DisplayName("팔로우 성공")
    void 팔로우_성공() throws Exception {
        CreateUserResponse createdUser1 = authUtil.register(user1.getEmail(), user1.getNickname(), user1.getPassword());
        CreateUserResponse createdUser2 = authUtil.register(user2.getEmail(), user2.getNickname(), user2.getPassword());
        // 로그인 후 팔로우
        String sessionId = authUtil.login(user1.getEmail(), user1.getPassword());
        Cookie cookie = new Cookie("SESSION", sessionId);
        String followUrl = String.format("http://localhost:%d/v1/follows/%d", port, createdUser2.getUserId());

        ResultActions response = mockMvc.perform(post(followUrl).cookie(cookie));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.followerId").value(createdUser1.getUserId()))
                .andExpect(jsonPath("$.data.followeeId").value(createdUser2.getUserId()));
    }

    @Test
    @DisplayName("팔로우 실패 - 자기 자신을 팔로잉 하는 경우")
    void 팔로우_실패_자기_자신_팔로우() throws Exception {
        CreateUserResponse createdUser = authUtil.register(user1.getEmail(), user1.getNickname(), user1.getPassword());
        // 로그인 후 팔로우
        String sessionId = authUtil.login(user1.getEmail(), user1.getPassword());
        Cookie cookie = new Cookie("SESSION", sessionId);
        String followUrl = String.format("http://localhost:%d/v1/follows/%d", port, createdUser.getUserId());

        ResultActions response = mockMvc.perform(post(followUrl).cookie(cookie));
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팔로우 실패 - 이미 팔로우 한 경우")
    void 팔로우_실패_이미_팔로우_한_경우() throws Exception {
        authUtil.register(user1.getEmail(), user1.getNickname(), user1.getPassword());
        CreateUserResponse createdUser2 = authUtil.register(user2.getEmail(), user2.getNickname(), user2.getPassword());
        // 로그인 후 팔로우
        String sessionId = authUtil.login(user1.getEmail(), user1.getPassword());
        follow(sessionId, createdUser2.getUserId());
        // 팔로우 한 유저를 한번 더 팔로우
        Cookie cookie = new Cookie("SESSION", sessionId);
        String followUrl = String.format("http://localhost:%d/v1/follows/%d", port, createdUser2.getUserId());

        ResultActions response = mockMvc.perform(post(followUrl).cookie(cookie));
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팔로우 삭제 성공")
    void 팔로우_삭제_성공() throws Exception {
        authUtil.register(user1.getEmail(), user1.getNickname(), user1.getPassword());
        CreateUserResponse createdUser2 = authUtil.register(user2.getEmail(), user2.getNickname(), user2.getPassword());
        // 로그인 후 팔로우
        String sessionId = authUtil.login(user1.getEmail(), user1.getPassword());
        follow(sessionId, createdUser2.getUserId());
        // 팔로우 삭제
        Cookie cookie = new Cookie("SESSION", sessionId);
        String followUrl = String.format("http://localhost:%d/v1/follows/%d", port, createdUser2.getUserId());

        ResultActions response = mockMvc.perform(delete(followUrl).cookie(cookie));
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("팔로우 삭제 실패 - 자기 자신을 팔로잉 하는 경우")
    void 팔로우_삭제_실패_자기_자신_팔로우_삭제() throws Exception {
        CreateUserResponse createdUser = authUtil.register(user1.getEmail(), user1.getNickname(), user1.getPassword());
        // 로그인 후 팔로우
        String sessionId = authUtil.login(user1.getEmail(), user1.getPassword());
        Cookie cookie = new Cookie("SESSION", sessionId);
        String followUrl = String.format("http://localhost:%d/v1/follows/%d", port, createdUser.getUserId());

        ResultActions response = mockMvc.perform(delete(followUrl).cookie(cookie));
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팔로우 삭제 실패 - 존재 하지 않는 팔로우 정보")
    void 팔로우_삭제_실패_존재하지_않는_팔로잉_정보() throws Exception {
        // 회원 가입 & 로그인
        authUtil.register(user1.getEmail(), user1.getNickname(), user1.getPassword());
        String sessionId = authUtil.login(user1.getEmail(), user1.getPassword());
        // 존재하지 않는 유저 아이디
        Long userId = 400L;
        // 팔로우 삭제
        Cookie cookie = new Cookie("SESSION", sessionId);
        String followUrl = String.format("http://localhost:%d/v1/follows/%d", port, userId);

        ResultActions response = mockMvc.perform(delete(followUrl).cookie(cookie));
        response.andExpect(status().isBadRequest());
    }
    /**
     *
     * @param sessionId 팔로우 하는 유저 세션
     * @param userId 팔로우 당하는 유저 아이디
     */
    private CreateFollowResponse follow(String sessionId, Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, "SESSION=" + sessionId);

        String followUrl = String.format("http://localhost:%d/v1/follows/%d", port, userId);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<ApiResponse<CreateFollowResponse>> response =
                restTemplate.exchange(followUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});

        return response.getBody().getData();
    }
}
