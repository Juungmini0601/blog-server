package org.logly.database.repository;

import java.util.List;

import org.logly.database.entity.UserEntity;
import org.logly.database.projection.PostDraftItem;

public interface PostDraftRepositoryCustom {
    List<PostDraftItem> findPostDraftsByUser(UserEntity user, Long lastPostDraftId);
}
