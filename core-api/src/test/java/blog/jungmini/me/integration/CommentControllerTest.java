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
import blog.jungmini.me.database.repository.UserRepository;
import blog.jungmini.me.dto.request.CreateCommentRequest;
import blog.jungmini.me.dto.request.CreatePostRequest;
import blog.jungmini.me.dto.request.UpdateCommentRequest;
import blog.jungmini.me.dto.response.CreateCommentResponse;
import blog.jungmini.me.dto.response.CreatePostResponse;
import blog.jungmini.me.util.AuthUtil;

public class CommentControllerTest extends AbstractTestContainerTest {
    AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil(restTemplate, port);
    }

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        // TODO 간혹 데이터베이스 초기화가 제대로 이루어지지 않는 경우가 있다.
        // MockMVC, JPA, Transactional간의 상관관계에 대해서 제대로 학습이 필요하다.
        userRepository.deleteAll();
    }

    UserEntity defaultUser = UserEntity.builder()
            .email("test@test.com")
            .nickname("test")
            .password("qwer12345")
            .build();

    CreatePostRequest defaultCreatePostRequest = new CreatePostRequest("title", "content", "url", true, null);

    @Test
    @DisplayName("댓글 생성 성공")
    void 댓글_생성_성공() throws Exception {
        // 회원 가입 및 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        // 게시글 생성
        CreatePostResponse createdPost = createPost(sessionId, defaultCreatePostRequest);

        String url = String.format("http://localhost:%d/v1/comments", port, createdPost.getPostId());
        CreateCommentRequest request = new CreateCommentRequest(createdPost.getPostId(), "test comment");
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(post(url)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk()).andExpect(jsonPath("$.data.postId").value(createdPost.getPostId()));
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void 댓글_수정_성공() throws Exception {
        // 회원 가입 및 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        // 게시글 생성
        CreatePostResponse createdPost = createPost(sessionId, defaultCreatePostRequest);
        // 댓글 생성
        CreateCommentResponse createdComment =
                createComment(sessionId, new CreateCommentRequest(createdPost.getPostId(), "test comment"));

        String url = String.format("http://localhost:%d/v1/comments/%d", port, createdComment.getCommentId());
        String updatedContent = "updated comment";

        UpdateCommentRequest request = new UpdateCommentRequest(updatedContent);
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(put(url).cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 다른 사람이 작성한 댓글")
    void 댓글_수정_실패_다른_사람이_작성한_댓글() throws Exception {
        // 첫번째 유저 회원가입 및 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        // 게시글 생성
        CreatePostResponse createdPost = createPost(sessionId, defaultCreatePostRequest);
        // 댓글 생성
        CreateCommentResponse createdComment =
                createComment(sessionId, new CreateCommentRequest(createdPost.getPostId(), "test comment"));

        // 다른 유저 회원가입 및 로그인
        UserEntity anotherUser = UserEntity.builder()
                .email("another@test.com")
                .nickname("another")
                .password("qwer12345")
                .build();

        authUtil.register(anotherUser.getEmail(), anotherUser.getNickname(), anotherUser.getPassword());
        String anotherSessionId = authUtil.login(anotherUser.getEmail(), anotherUser.getPassword());

        String url = String.format("http://localhost:%d/v1/comments/%d", port, createdComment.getCommentId());
        String updatedContent = "updated comment";

        UpdateCommentRequest request = new UpdateCommentRequest(updatedContent);
        Cookie cookie = new Cookie("SESSION", anotherSessionId);

        ResultActions response = mockMvc.perform(put(url).cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void 댓글_삭제_성공() throws Exception {
        // 회원 가입 및 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        // 게시글 생성
        CreatePostResponse createdPost = createPost(sessionId, defaultCreatePostRequest);
        // 댓글 생성
        CreateCommentResponse createdComment =
                createComment(sessionId, new CreateCommentRequest(createdPost.getPostId(), "test comment"));

        String url = String.format("http://localhost:%d/v1/comments/%d", port, createdComment.getCommentId());
        Cookie cookie = new Cookie("SESSION", sessionId);

        ResultActions response = mockMvc.perform(delete(url).cookie(cookie));

        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 자신이 작성한 댓글이 아닌 경우")
    void 댓글_삭제_실패_자신이_작성하지_않은_댓글() throws Exception {
        // 첫번째 유저 회원가입 및 로그인
        authUtil.register(defaultUser.getEmail(), defaultUser.getNickname(), defaultUser.getPassword());
        String sessionId = authUtil.login(defaultUser.getEmail(), defaultUser.getPassword());
        // 게시글 생성
        CreatePostResponse createdPost = createPost(sessionId, defaultCreatePostRequest);
        // 댓글 생성
        CreateCommentResponse createdComment =
                createComment(sessionId, new CreateCommentRequest(createdPost.getPostId(), "test comment"));

        // 다른 유저 회원가입 및 로그인
        UserEntity anotherUser = UserEntity.builder()
                .email("another@test.com")
                .nickname("another")
                .password("qwer12345")
                .build();

        authUtil.register(anotherUser.getEmail(), anotherUser.getNickname(), anotherUser.getPassword());
        String anotherSessionId = authUtil.login(anotherUser.getEmail(), anotherUser.getPassword());

        String url = String.format("http://localhost:%d/v1/comments/%d", port, createdComment.getCommentId());
        Cookie cookie = new Cookie("SESSION", anotherSessionId);

        ResultActions response = mockMvc.perform(delete(url).cookie(cookie));

        response.andExpect(status().isForbidden());
    }

    private CreateCommentResponse createComment(String sessionId, CreateCommentRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, "SESSION=" + sessionId);

        String createCommentUrl = String.format("http://localhost:%d/v1/comments", port);

        HttpEntity<CreateCommentRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ApiResponse<CreateCommentResponse>> response = restTemplate.exchange(
                createCommentUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

        return response.getBody().getData();
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
