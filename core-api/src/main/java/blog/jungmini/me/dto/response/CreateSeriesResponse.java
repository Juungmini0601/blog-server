package blog.jungmini.me.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import blog.jungmini.me.database.entity.SeriesEntity;

@Getter
public class CreateSeriesResponse {
    private Long seriesId;
    private Long userId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
                .userId(entity.getUserId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
