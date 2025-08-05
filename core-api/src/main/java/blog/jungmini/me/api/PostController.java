package blog.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.jungmini.me.application.PostService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.PostEntity;
import blog.jungmini.me.dto.request.CreatePostRequest;
import blog.jungmini.me.dto.response.CreatePostResponse;
import blog.jungmini.me.security.model.CustomUserDetails;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/v1/posts")
    public ApiResponse<CreatePostResponse> create(
            Authentication authentication, @Valid @RequestBody CreatePostRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostEntity postRequest = request.toEntity();

        PostEntity createdPost = postService.create(details.getUserId(), postRequest);
        return ApiResponse.success(CreatePostResponse.fromEntity(createdPost));
    }
}
