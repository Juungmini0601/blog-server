package org.logly.database.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeriesItem {
    private Long seriesId;
    private String name;
    private Long postCount;
}
