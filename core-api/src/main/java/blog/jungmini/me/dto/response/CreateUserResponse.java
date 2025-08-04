package blog.jungmini.me.dto.response;

import lombok.Builder;
import lombok.Getter;

import blog.jungmini.me.database.entity.UserEntity;

@Getter
public class CreateUserResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String githubUrl;
    private String introduction;

    public CreateUserResponse() {}

    @Builder
    public CreateUserResponse(
            Long userId, String email, String nickname, String profileImageUrl, String githubUrl, String introduction) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.githubUrl = githubUrl;
        this.introduction = introduction;
    }

    public static CreateUserResponse fromEntity(UserEntity entity) {
        return CreateUserResponse.builder()
                .userId(entity.getUserId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .profileImageUrl(entity.getProfileImageUrl())
                .githubUrl(entity.getGithubUrl())
                .introduction(entity.getIntroduction())
                .build();
    }
}
