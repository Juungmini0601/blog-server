package blog.jungmini.me.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.AbstractTestContainerTest;
import blog.jungmini.me.application.UserService;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.dto.request.LoginRequest;
import blog.jungmini.me.util.AuthUtil;

@Transactional
public class AuthControllerTest extends AbstractTestContainerTest {

    AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil(restTemplate, port);
    }

    @Autowired
    UserService userService;

    UserEntity defaultUser = UserEntity.builder()
            .email("test@test.com")
            .nickname("test")
            .password("qwer12345")
            .build();

    @Test
    @DisplayName("로그인 성공")
    void 로그인_성공() throws Exception {
        userService.register(defaultUser);
        LoginRequest request = new LoginRequest("test@test.com", "qwer12345");
        String url = String.format("http://localhost:%d/v1/auth/login", port);

        ResultActions response = mockMvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sessionId").isString());
    }

    @Test
    @DisplayName("로그인 실패 - 일치하지 않는 비밀번호")
    void 로그인_실패_일치하지_않는_비밀번호() throws Exception {
        userService.register(defaultUser);
        LoginRequest request = new LoginRequest("test@test.com", "invalidPassword");
        String url = String.format("http://localhost:%d/v1/auth/login", port);

        ResultActions response = mockMvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void 로그아웃_성공() throws Exception {
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());

        String logoutUrl = String.format("http://localhost:%d/v1/auth/logout", port);
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(post(logoutUrl).cookie(cookie));

        response.andExpect(status().isOk());
    }
}
