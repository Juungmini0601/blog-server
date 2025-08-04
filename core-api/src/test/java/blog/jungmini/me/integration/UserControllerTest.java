package blog.jungmini.me.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.AbstractTestContainerTest;
import blog.jungmini.me.application.UserService;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.dto.request.CreateUserRequest;

import blog.jungmini.common.error.ErrorCode;

@Transactional
public class UserControllerTest extends AbstractTestContainerTest {
    @Autowired
    UserService userService;

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
        String url = String.format("http://localhost:%d/v1/users", port);

        ResultActions response = mockMvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk()).andExpect(jsonPath("$.data.email").value(defaultUser.getEmail()));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void 회원_가입_실패_중복된_이메일() throws Exception {
        CreateUserRequest request =
                new CreateUserRequest(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        userService.register(request.toEntity());
        String url = String.format("http://localhost:%d/v1/users", port);

        ResultActions response = mockMvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.E400.name()))
                .andExpect(jsonPath("$.error.data").isString());
    }
}
