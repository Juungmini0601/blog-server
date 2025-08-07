package blog.jungmini.me.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import blog.jungmini.me.AbstractTestContainerTest;
import blog.jungmini.me.common.error.ErrorCode;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.repository.UserRepository;
import blog.jungmini.me.dto.request.CreateUserRequest;
import blog.jungmini.me.dto.request.UpdateUserRequest;
import blog.jungmini.me.util.AuthUtil;

public class UserControllerTest extends AbstractTestContainerTest {

    AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil(restTemplate, port);
    }

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    UserEntity defaultUser = UserEntity.builder()
            .email("test@test.com")
            .nickname("test")
            .password("qwer12345")
            .build();

    @Test
    @DisplayName("회원가입 성공")
    void 회원_가입_성공() throws Exception {
        CreateUserRequest request =
                new CreateUserRequest(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String url = String.format("http://localhost:%d/v1/users/register", port);

        ResultActions response = mockMvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk()).andExpect(jsonPath("$.data.email").value(defaultUser.getEmail()));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void 회원_가입_실패_중복된_이메일() throws Exception {
        CreateUserRequest request =
                new CreateUserRequest(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String url = String.format("http://localhost:%d/v1/users/register", port);

        ResultActions response = mockMvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.E400.name()))
                .andExpect(jsonPath("$.error.data").isString());
    }

    @Test
    @DisplayName("로그인한 회원 정보 조회 성공")
    void 로그인_회원_정보_조회_성공() throws Exception {
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());

        String url = String.format("http://localhost:%d/v1/users/me", port);
        Cookie cookie = new Cookie("SESSION", sessionId);
        ResultActions response = mockMvc.perform(get(url).cookie(cookie));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").isNumber())
                .andExpect(jsonPath("$.data.email").value(defaultUser.getEmail()))
                .andExpect(jsonPath("$.data.nickname").value(defaultUser.getNickname()));
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void 회원_정보_수정_성공() throws Exception {
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());

        String url = String.format("http://localhost:%d/v1/users/update", port);
        Cookie cookie = new Cookie("SESSION", sessionId);

        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname("newNickname")
                .profileImageUrl("newProfileImageUrl")
                .githubUrl("newGithubUrl")
                .introduction("newIntroduction")
                .build();

        ResultActions response = mockMvc.perform(put(url).cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").isNumber())
                .andExpect(jsonPath("$.data.email").value(defaultUser.getEmail()))
                .andExpect(jsonPath("$.data.nickname").value(request.getNickname()))
                .andExpect(jsonPath("$.data.profileImageUrl").value(request.getProfileImageUrl()))
                .andExpect(jsonPath("$.data.githubUrl").value(request.getGithubUrl()))
                .andExpect(jsonPath("$.data.introduction").value(request.getIntroduction()));
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void 회원_탈퇴_성공() throws Exception {
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());

        String url = String.format("http://localhost:%d/v1/users/remove", port);
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(delete(url).cookie(cookie));
        response.andExpect(status().isOk());
    }
}
