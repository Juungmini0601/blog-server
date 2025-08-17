package org.logly.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.logly.application.PostLikeService;
import org.logly.database.entity.PostLikeEntity;
import org.logly.dto.response.CreatePostLikeResponse;
import org.logly.response.ApiResponse;
import org.logly.security.model.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/v1/posts/{postId}/like")
    public ApiResponse<CreatePostLikeResponse> like(Authentication authentication, @PathVariable Long postId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostLikeEntity like = postLikeService.like(details.getUserId(), postId);

        return ApiResponse.success(CreatePostLikeResponse.fromEntity(like));
    }

    @DeleteMapping("/v1/posts/{postId}/like")
    public ApiResponse<?> dislike(Authentication authentication, @PathVariable Long postId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        postLikeService.disLike(details.getUserId(), postId);

        return ApiResponse.success();
    }
}
