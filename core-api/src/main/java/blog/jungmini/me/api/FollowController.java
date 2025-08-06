package blog.jungmini.me.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.jungmini.me.application.FollowService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.FollowEntity;
import blog.jungmini.me.dto.response.CreateFollowResponse;
import blog.jungmini.me.security.model.CustomUserDetails;

@RestController
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/v1/follows/{userId}")
    public ApiResponse<CreateFollowResponse> follow(Authentication authentication, @PathVariable Long userId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        FollowEntity follow = followService.follow(details.getUserId(), userId);

        return ApiResponse.success(CreateFollowResponse.fromEntity(follow));
    }

    @DeleteMapping("/v1/follows/{userId}")
    public ApiResponse<?> unFollow(Authentication authentication, @PathVariable Long userId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        followService.unFollow(details.getUserId(), userId);

        return ApiResponse.success();
    }
}
