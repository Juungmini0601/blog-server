package blog.jungmini.me.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import blog.jungmini.me.database.entity.UserEntity;

@Getter
public class UpdateUserResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String githubUrl;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UpdateUserResponse() {}

    @Builder
    public UpdateUserResponse(
            Long userId,
            String email,
            String nickname,
            String profileImageUrl,
            String githubUrl,
            String introduction,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.githubUrl = githubUrl;
        this.introduction = introduction;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UpdateUserResponse fromEntity(UserEntity entity) {
        return UpdateUserResponse.builder()
                .userId(entity.getUserId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .profileImageUrl(entity.getProfileImageUrl())
                .githubUrl(entity.getGithubUrl())
                .introduction(entity.getIntroduction())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
