package org.logly.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/v1/posts")
    public CursorResponse<PostItem, Long> getPosts(@RequestParam(required = false) Long lastPostId) {
        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        return postService.getPostList(lastPostId);
    }

    @GetMapping("/v1/posts/users/{userId}")
    public CursorResponse<PostItem, Long> getUserPosts(
            @RequestParam(required = false) Long lastPostId, @PathVariable Long userId) {
        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        return postService.getPostListByUserId(userId, lastPostId);
    }

    @GetMapping("/v1/posts/series/{seriesId}")
    public CursorResponse<PostItem, Long> getSeriesPosts(
            @RequestParam(required = false) Long lastPostId, @PathVariable Long seriesId) {
        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        return postService.getPostListBySeriesId(seriesId, lastPostId);
    }

    @GetMapping("/v1/posts/{postId}")
    public ApiResponse<GetPostResponse> getPostById(@PathVariable Long postId, Authentication authentication) {
        if (authentication == null) {
            return ApiResponse.success(GetPostResponse.fromEntity(postService.getById(postId, null)));
        }

        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostDetail post = postService.getById(postId, details);

        return ApiResponse.success(GetPostResponse.fromEntity(post));
    }

    @PostMapping("/v1/posts")
    public ApiResponse<CreatePostResponse> create(
            Authentication authentication, @Valid @RequestBody CreatePostRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostEntity createdPost = postService.create(details, request);

        return ApiResponse.success(CreatePostResponse.fromEntity(createdPost));
    }

    @PutMapping("/v1/posts/{postId}")
    public ApiResponse<UpdatePostResponse> update(
            Authentication authentication, @PathVariable Long postId, @Valid @RequestBody UpdatePostRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostEntity updatedPost = postService.update(details, postId, request);

        return ApiResponse.success(UpdatePostResponse.fromEntity(updatedPost));
    }

    @DeleteMapping("/v1/posts/{postId}")
    public ApiResponse<?> delete(Authentication authentication, @PathVariable Long postId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        postService.remove(details, postId);

        return ApiResponse.success();
    }
}
