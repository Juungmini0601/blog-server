package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.database.entity.FollowEntity;

@Getter
public class CreateFollowResponse {
    private Long followId;
    private Long followerId;
    private Long followeeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreateFollowResponse() {}

    @Builder
    public CreateFollowResponse(
            Long followId, Long followerId, Long followeeId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.followId = followId;
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CreateFollowResponse fromEntity(FollowEntity entity) {
        return CreateFollowResponse.builder()
                .followId(entity.getFollowId())
                .followerId(entity.getFollowerId())
                .followeeId(entity.getFolloweeId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
