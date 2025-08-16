package org.logly.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.logly.application.PostService;
import org.logly.database.entity.PostEntity;
import org.logly.database.projection.PostDetail;
import org.logly.database.projection.PostItem;
import org.logly.dto.request.CreatePostRequest;
import org.logly.dto.request.UpdatePostRequest;
import org.logly.dto.response.CreatePostResponse;
import org.logly.dto.response.GetPostResponse;
import org.logly.dto.response.UpdatePostResponse;
import org.logly.response.ApiResponse;
import org.logly.response.CursorResponse;
import org.logly.security.model.CustomUserDetails;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/v1/posts")
    public CursorResponse<PostItem, Long> getPosts(@RequestParam(required = false) Long lastPostId) {
        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        return postService.getPostList(lastPostId);
    }

    @GetMapping("/v1/posts/{postId}")
    public ApiResponse<GetPostResponse> getPostById(@PathVariable Long postId, Authentication authentication) {
        if (authentication == null) {
            return ApiResponse.success(GetPostResponse.fromEntity(postService.getById(postId, null)));
        }

        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostDetail post = postService.getById(postId, details.getUserId());

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
