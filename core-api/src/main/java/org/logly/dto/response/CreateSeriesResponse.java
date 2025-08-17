package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.logly.database.entity.SeriesEntity;

@ToString
@Getter
public class CreateSeriesResponse {
    private Long seriesId;
    private Long userId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreateSeriesResponse() {}

    @Builder
    public CreateSeriesResponse(
            Long seriesId, Long userId, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.seriesId = seriesId;
        this.userId = userId;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CreateSeriesResponse fromEntity(SeriesEntity entity) {
        return CreateSeriesResponse.builder()
                .seriesId(entity.getSeriesId())
                .userId(entity.getUser().getUserId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
