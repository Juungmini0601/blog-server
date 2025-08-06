package blog.jungmini.me.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.jungmini.me.application.PostLikeService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.PostLikeEntity;
import blog.jungmini.me.dto.response.CreatePostLikeResponse;
import blog.jungmini.me.security.model.CustomUserDetails;

@RestController
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

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
