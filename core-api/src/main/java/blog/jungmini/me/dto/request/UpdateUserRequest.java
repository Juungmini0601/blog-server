package blog.jungmini.me.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserRequest {
    @NotBlank(message = "닉네임은 필수 입력값입니다")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요")
    private String nickname;

    @Size(max = 255, message = "프로필 이미지 URL은 255자를 초과할 수 없습니다")
    private String profileImageUrl;

    @Size(max = 255, message = "Github URL은 255자를 초과할 수 없습니다")
    private String githubUrl;

    @Size(max = 100, message = "자기소개는 100자를 초과할 수 없습니다")
    private String introduction;

    public UpdateUserRequest() {}

    @Builder
    public UpdateUserRequest(String nickname, String profileImageUrl, String githubUrl, String introduction) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.githubUrl = githubUrl;
        this.introduction = introduction;
    }
}
