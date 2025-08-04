package blog.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import blog.jungmini.me.application.UserService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.dto.request.CreateUserRequest;
import blog.jungmini.me.dto.request.UpdateUserRequest;
import blog.jungmini.me.dto.response.CreateUserResponse;
import blog.jungmini.me.dto.response.GetUserResponse;
import blog.jungmini.me.dto.response.UpdateUserResponse;
import blog.jungmini.me.security.model.CustomUserDetails;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/users/me")
    public ApiResponse<GetUserResponse> me(Authentication authentication) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userService.getUserById(details.getUserId());

        return ApiResponse.success(GetUserResponse.fromEntity(user));
    }

    @PostMapping("/v1/users/register")
    public ApiResponse<CreateUserResponse> register(@RequestBody @Valid CreateUserRequest request) {
        UserEntity user = request.toEntity();
        UserEntity registered = userService.register(user);

        return ApiResponse.success(CreateUserResponse.fromEntity(registered));
    }

    @PutMapping("/v1/users/update")
    public ApiResponse<UpdateUserResponse> update(
            Authentication authentication, @RequestBody @Valid UpdateUserRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userService.update(details.getUserId(), request);

        return ApiResponse.success(UpdateUserResponse.fromEntity(user));
    }

    @DeleteMapping("/v1/users/remove")
    public ApiResponse<?> remove(Authentication authentication) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        userService.remove(details.getUserId());

        return ApiResponse.success();
    }
}
