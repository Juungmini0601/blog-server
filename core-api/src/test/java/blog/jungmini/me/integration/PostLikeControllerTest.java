package blog.jungmini.me.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;

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
import blog.jungmini.me.database.repository.PostLikeRepository;
import blog.jungmini.me.database.repository.PostRepository;
import blog.jungmini.me.database.repository.UserRepository;
import blog.jungmini.me.dto.request.CreatePostRequest;
import blog.jungmini.me.dto.response.CreatePostResponse;
import blog.jungmini.me.util.AuthUtil;

public class PostLikeControllerTest extends AbstractTestContainerTest {
    AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil(restTemplate, port);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    UserEntity defaultUser = UserEntity.builder()
            .email("test@test.com")
            .nickname("test")
            .password("qwer12345")
            .build();

    CreatePostRequest defaultPostCreateRequest = CreatePostRequest.builder()
            .title("testTitle")
            .content("testContent")
            .build();

    @BeforeEach
    void tearDown() {
        postLikeRepository.deleteAll();
        postLikeRepository.flush();
        postRepository.deleteAll();
        postRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    @DisplayName("게시글 좋아요 성공")
    void 게시글_좋아요_성공() throws Exception {
        // 회원 가입 & 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        // 게시글 생성
        CreatePostResponse createPostResponse = createPost(sessionId, defaultPostCreateRequest);

        String url = String.format("http://localhost:%d/v1/posts/%d/like", port, createPostResponse.getPostId());
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(post(url).cookie(cookie));

        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 좋아요 실패 - 존재하지 않는 게시글")
    void 게시글_좋아요_실패_존재하지_않는_게시글() throws Exception {
        // 회원 가입 & 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());

        Long postId = 999L;

        String url = String.format("http://localhost:%d/v1/posts/%d/like", port, postId);
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(post(url).cookie(cookie));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 좋아요 취소 성공")
    void 게시글_좋아요_취소_성공() throws Exception {
        // 회원 가입 & 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        // 게시글 생성 및 좋아요
        CreatePostResponse createPostResponse = createPost(sessionId, defaultPostCreateRequest);
        like(sessionId, createPostResponse.getPostId());
        // 게시글 좋아요 취소
        String url = String.format("http://localhost:%d/v1/posts/%d/like", port, createPostResponse.getPostId());
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(delete(url).cookie(cookie));

        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 좋아요 취소 실패 - 좋아요 정보 없음")
    void 게시글_좋아요_취소_실패_좋아요_정보_없음() throws Exception {
        // 회원 가입 & 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        // 게시글 생성 및 좋아요
        CreatePostResponse createPostResponse = createPost(sessionId, defaultPostCreateRequest);
        // 게시글 좋아요 취소
        String url = String.format("http://localhost:%d/v1/posts/%d/like", port, createPostResponse.getPostId());
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(delete(url).cookie(cookie));

        response.andExpect(status().isBadRequest());
    }

    private void like(String sessionId, Long postId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "SESSION=" + sessionId);

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        String url = String.format("http://localhost:%d/v1/posts/%d/like", port, postId);

        restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
    }

    private CreatePostResponse createPost(String sessionId, CreatePostRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, "SESSION=" + sessionId);

        String createPostUrl = String.format("http://localhost:%d/v1/posts", port);

        HttpEntity<CreatePostRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ApiResponse<CreatePostResponse>> response = restTemplate.exchange(
                createPostUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

        return response.getBody().getData();
    }
}
