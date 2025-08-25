package org.logly.database.repository;

import java.util.List;

import org.logly.database.projection.CommentItem;

public interface CommentRepositoryCustom {
    List<CommentItem> findCommentsByPostId(Long postId, Long lastCommentId);
}
