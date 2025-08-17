package org.logly.database.repository;

import java.util.List;

import org.logly.database.entity.SeriesEntity;
import org.logly.database.projection.PostItem;

public interface PostRepositoryCustom {
    public void setSeriesIdNullBySeries(SeriesEntity series);

    List<PostItem> findPostItemsByUserId(Long userId, Long lastPostId);

    List<PostItem> findPostItemsBySeriesId(Long seriesId, Long lastPostId);

    List<PostItem> findPosts(Long lastPostId);
}
