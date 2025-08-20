package org.logly.database.repository;

import java.util.List;

import org.logly.database.entity.SeriesEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.projection.PostItem;

public interface PostRepositoryCustom {
    public void setSeriesIdNullBySeries(SeriesEntity series);

    List<PostItem> findPostItemsByUser(UserEntity user, Long lastPostId);

    List<PostItem> findPostItemsBySeries(SeriesEntity series, Long lastPostId);

    List<PostItem> findPosts(Long lastPostId);

    List<PostItem> searchPosts(String keyword, Long lastPostId);
}
