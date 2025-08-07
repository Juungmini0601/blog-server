package blog.jungmini.me.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import blog.jungmini.me.application.FollowService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.common.response.CursorResponse;
import blog.jungmini.me.database.entity.FollowEntity;
import blog.jungmini.me.database.projection.UserFollowItem;
import blog.jungmini.me.dto.response.CreateFollowResponse;
import blog.jungmini.me.security.model.CustomUserDetails;

@RestController
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // userId를 팔로우 하고 있는 유저들의 리스트 반환
    @GetMapping("/v1/follows/{userId}")
    public CursorResponse<UserFollowItem, Long> getFollowersList(
            @PathVariable Long userId, @RequestParam(required = false) Long lastFollowId) {
        if (lastFollowId == null) {
            lastFollowId = Long.MAX_VALUE;
        }

        return followService.getFollowerList(userId, lastFollowId);
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
