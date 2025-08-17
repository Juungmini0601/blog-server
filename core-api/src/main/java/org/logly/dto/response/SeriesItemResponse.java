package org.logly.dto.response;

import lombok.*;

import org.logly.database.entity.SeriesEntity;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesItemResponse {
    private Long seriesId;
    private String name;
    private Long postCount;

    public static SeriesItemResponse fromEntity(SeriesEntity item) {
        return builder()
                .seriesId(item.getSeriesId())
                .name(item.getName())
                .postCount(item.getPostCount())
                .build();
    }
}
