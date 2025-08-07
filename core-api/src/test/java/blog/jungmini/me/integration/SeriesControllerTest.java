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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.servlet.ResultActions;

import blog.jungmini.me.AbstractTestContainerTest;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.repository.SeriesRepository;
import blog.jungmini.me.database.repository.UserRepository;
import blog.jungmini.me.dto.request.CreateSeriesRequest;
import blog.jungmini.me.dto.request.UpdateSeriesRequest;
import blog.jungmini.me.dto.response.CreateSeriesResponse;
import blog.jungmini.me.util.AuthUtil;

public class SeriesControllerTest extends AbstractTestContainerTest {
    AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil(restTemplate, port);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @AfterEach
    void tearDown() {
        seriesRepository.deleteAll();
        userRepository.deleteAll();
    }

    UserEntity defaultUser = UserEntity.builder()
            .email("test@test.com")
            .nickname("test")
            .password("qwer12345")
            .build();

    @Test
    @DisplayName("시리즈 생성 성공")
    void 시리즈_생성_성공() throws Exception {
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());

        String url = String.format("http://localhost:%d/v1/series", port);
        Cookie cookie = new Cookie("SESSION", sessionId);
        CreateSeriesRequest request = new CreateSeriesRequest("testSeries");

        ResultActions response = mockMvc.perform(post(url)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.seriesId").isNumber())
                .andExpect(jsonPath("$.data.name").value(request.getName()));
    }

    @Test
    @DisplayName("시리즈 생성 실패 - 인증 되지 않은 유저")
    void 시리즈_생성_실패_인증되지_않은_유저() throws Exception {
        String url = String.format("http://localhost:%d/v1/series", port);
        CreateSeriesRequest request = new CreateSeriesRequest("testSeries");

        ResultActions response = mockMvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("시리즈 수정 성공")
    void 시리즈_수정_성공() throws Exception {
        // 회원 가입 & 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        Cookie cookie = new Cookie("SESSION", sessionId);
        // 시리즈 생성
        CreateSeriesResponse series = createSeries(sessionId, "testSeries");

        // 시리즈 수정
        UpdateSeriesRequest request = new UpdateSeriesRequest("newSeriesName");
        String url = String.format("http://localhost:%d/v1/series/%d", port, series.getSeriesId());

        ResultActions response = mockMvc.perform(put(url).cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.seriesId").isNumber())
                .andExpect(jsonPath("$.data.name").value(request.getName()));
    }

    @Test
    @DisplayName("시리즈 수정 실패 - 다른 유저의 시리즈 수정 요청")
    void 시리즈_수정_실패_403() throws Exception {
        authUtil.register("user1@email.com", "user1", "qwer12345");
        String session1 = authUtil.login("user1@email.com", "qwer12345");
        CreateSeriesResponse series = createSeries(session1, "series");
        // 다른 유저가 회원가입 하고 로그인
        authUtil.register("user2@email.com", "user2", "qwer12345");
        String session2 = authUtil.login("user2@email.com", "qwer12345");
        Cookie cookie = new Cookie("SESSION", session2);
        // 시리즈 수정
        UpdateSeriesRequest request = new UpdateSeriesRequest("newSeriesName");
        String url = String.format("http://localhost:%d/v1/series/%d", port, series.getSeriesId());

        ResultActions response = mockMvc.perform(put(url).cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("시리즈 삭제 성공")
    void 시리즈_삭제_성공() throws Exception {
        // 회원 가입 & 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        Cookie cookie = new Cookie("SESSION", sessionId);
        // 시리즈 생성
        CreateSeriesResponse series = createSeries(sessionId, "testSeries");

        // 시리즈 삭제
        String url = String.format("http://localhost:%d/v1/series/%d", port, series.getSeriesId());

        ResultActions response = mockMvc.perform(delete(url).cookie(cookie));

        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("시리즈 삭제 실패 - 다른 유저의 시리즈 삭제 요청")
    void 시리즈_삭제_실패_403() throws Exception {
        authUtil.register("user1@email.com", "user1", "qwer12345");
        String session1 = authUtil.login("user1@email.com", "qwer12345");
        CreateSeriesResponse series = createSeries(session1, "series");
        // 다른 유저가 회원가입 하고 로그인
        authUtil.register("user2@email.com", "user2", "qwer12345");
        String session2 = authUtil.login("user2@email.com", "qwer12345");
        Cookie cookie = new Cookie("SESSION", session2);
        // 시리즈 삭제
        String url = String.format("http://localhost:%d/v1/series/%d", port, series.getSeriesId());

        ResultActions response = mockMvc.perform(delete(url).cookie(cookie));

        response.andExpect(status().isForbidden());
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
