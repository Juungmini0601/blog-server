package org.logly.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;

import org.logly.database.entity.UserEntity;

@Getter
public class CreateUserRequest {
    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력값입니다")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요")
    private String password;

    public CreateUserRequest() {}

    public CreateUserRequest(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
    }
}
