package org.logly.dto.response;

import lombok.*;

import org.logly.database.projection.SeriesItem;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesItemResponse {
    private Long seriesId;
    private String name;
    private Long postCount;

    public static SeriesItemResponse fromEntity(SeriesItem item) {
        return builder()
                .seriesId(item.getSeriesId())
                .name(item.getName())
                .postCount(item.getPostCount())
                .build();
    }
}
