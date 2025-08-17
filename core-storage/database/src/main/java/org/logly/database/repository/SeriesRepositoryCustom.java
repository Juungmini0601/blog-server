package org.logly.database.repository;

import java.util.List;

import org.logly.database.projection.SeriesItem;

public interface SeriesRepositoryCustom {
    List<SeriesItem> findSeriesItemsByUserId(Long userId);
}
