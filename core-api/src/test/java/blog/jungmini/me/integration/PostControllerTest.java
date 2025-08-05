package blog.jungmini.me.integration;

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
import blog.jungmini.me.database.repository.PostRepository;
import blog.jungmini.me.database.repository.SeriesRepository;
import blog.jungmini.me.database.repository.UserRepository;
import blog.jungmini.me.dto.request.CreatePostRequest;
import blog.jungmini.me.dto.request.CreateSeriesRequest;
import blog.jungmini.me.dto.response.CreateSeriesResponse;
import blog.jungmini.me.util.AuthUtil;

public class PostControllerTest extends AbstractTestContainerTest {
    AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil(restTemplate, port);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
        postRepository.flush();
        seriesRepository.deleteAll();
        seriesRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    UserEntity defaultUser = UserEntity.builder()
            .email("test@test.com")
            .nickname("test")
            .password("qwer12345")
            .build();

    @Test
    @DisplayName("게시글 생성 성공 - (Series X)")
    void 게시글_생성_성공_시리즈_X() throws Exception {
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());

        String url = String.format("http://localhost:%d/v1/posts", port);
        Cookie cookie = new Cookie("SESSION", sessionId);
        CreatePostRequest request = new CreatePostRequest("testPost", "testTitle", "testContent", true, null);

        ResultActions response = mockMvc.perform(post(url)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(request.getTitle()))
                .andExpect(jsonPath("$.data.content").value(request.getContent()))
                .andExpect(jsonPath("$.data.isPublic").isBoolean());
    }

    @Test
    @DisplayName("게시글 생성 성공 - (Series O)")
    void 게시글_생성_성공_시리즈_O() throws Exception {
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        CreateSeriesResponse series = createSeries(sessionId, "testSeries");

        String url = String.format("http://localhost:%d/v1/posts", port);
        Cookie cookie = new Cookie("SESSION", sessionId);
        CreatePostRequest request =
                new CreatePostRequest("testPost", "testTitle", "testContent", true, series.getSeriesId());

        ResultActions response = mockMvc.perform(post(url)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(request.getTitle()))
                .andExpect(jsonPath("$.data.content").value(request.getContent()))
                .andExpect(jsonPath("$.data.isPublic").isBoolean())
                .andExpect(jsonPath("$.data.seriesId").value(series.getSeriesId()));
    }

    @Test
    @DisplayName("게시글 생성 실패 - 다른 사람의 시리즈 ID")
    void 게시글_생성_실패_시리즈_아이디_오류() throws Exception {
        authUtil.register("user1@email.com", "user1", "qwer12345");
        String session1 = authUtil.login("user1@email.com", "qwer12345");
        CreateSeriesResponse series = createSeries(session1, "series");
        // 다른 유저가 회원가입 하고 로그인
        authUtil.register("user2@email.com", "user2", "qwer12345");
        String session2 = authUtil.login("user2@email.com", "qwer12345");
        Cookie cookie = new Cookie("SESSION", session2);
        // 다른 사람의 시리즈 ID로 게시글 생성 요청
        String url = String.format("http://localhost:%d/v1/posts", port);
        CreatePostRequest request =
                new CreatePostRequest("testPost", "testTitle", "testContent", true, series.getSeriesId());

        ResultActions response = mockMvc.perform(post(url)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isBadRequest());
    }

    private CreateSeriesResponse createSeries(String sessionId, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, "SESSION=" + sessionId);

        CreateSeriesRequest request = new CreateSeriesRequest(name);
        String createSeriesUrl = String.format("http://localhost:%d/v1/series", port);

        HttpEntity<CreateSeriesRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ApiResponse<CreateSeriesResponse>> response = restTemplate.exchange(
                createSeriesUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

        return response.getBody().getData();
    }
}
