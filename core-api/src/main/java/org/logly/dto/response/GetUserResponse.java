package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.database.entity.UserEntity;

@Getter
public class GetUserResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String githubUrl;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GetUserResponse() {}

    @Builder
    public GetUserResponse(
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

    public static GetUserResponse fromEntity(UserEntity entity) {
        return GetUserResponse.builder()
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
