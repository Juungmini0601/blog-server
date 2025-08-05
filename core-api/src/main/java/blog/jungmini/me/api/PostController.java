package blog.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import blog.jungmini.me.application.PostService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.PostEntity;
import blog.jungmini.me.dto.request.CreatePostRequest;
import blog.jungmini.me.dto.request.UpdatePostRequest;
import blog.jungmini.me.dto.response.CreatePostResponse;
import blog.jungmini.me.dto.response.GetPostResponse;
import blog.jungmini.me.dto.response.UpdatePostResponse;
import blog.jungmini.me.security.model.CustomUserDetails;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/v1/posts/{postId}")
    public ApiResponse<GetPostResponse> getPostById(@PathVariable Long postId) {
        PostEntity post = postService.getById(postId);

        return ApiResponse.success(GetPostResponse.fromEntity(post));
    }

    @PostMapping("/v1/posts")
    public ApiResponse<CreatePostResponse> create(
            Authentication authentication, @Valid @RequestBody CreatePostRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostEntity postRequest = request.toEntity();

        PostEntity createdPost = postService.create(details.getUserId(), postRequest);
        return ApiResponse.success(CreatePostResponse.fromEntity(createdPost));
    }

    @PutMapping("/v1/posts/{postId}")
    public ApiResponse<UpdatePostResponse> update(
            Authentication authentication, @PathVariable Long postId, @Valid @RequestBody UpdatePostRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostEntity postRequest = request.toEntity(postId);

        PostEntity updatedPost = postService.update(details.getUserId(), postRequest);
        return ApiResponse.success(UpdatePostResponse.fromEntity(updatedPost));
    }

    @DeleteMapping("/v1/posts/{postId}")
    public ApiResponse<?> delete(Authentication authentication, @PathVariable Long postId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        postService.remove(details.getUserId(), postId);

        return ApiResponse.success();
    }
}
