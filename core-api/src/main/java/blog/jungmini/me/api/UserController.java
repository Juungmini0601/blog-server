package blog.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.jungmini.me.application.UserService;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.dto.request.CreateUserRequest;
import blog.jungmini.me.dto.response.CreateUserResponse;

import blog.jungmini.common.response.ApiResponse;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/v1/users")
    public ApiResponse<CreateUserResponse> register(@RequestBody @Valid CreateUserRequest request) {
        UserEntity user = request.toEntity();
        UserEntity registered = userService.register(user);

        return ApiResponse.success(CreateUserResponse.fromEntity(registered));
    }
}
