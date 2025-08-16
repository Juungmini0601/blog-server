package org.logly.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.logly.database.entity.CommentEntity;
import org.logly.database.projection.CommentItem;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    @Query(
            value =
                    """
            SELECT c.comment_id, c.post_id, c.content, c.created_at, c.user_id, u.nickname, u.profile_image_url
            FROM comments c
            JOIN users u ON c.user_id = u.user_id
            WHERE c.post_id = :postId AND c.comment_id < :lastCommentId
            ORDER BY c.comment_id DESC
            LIMIT 20
            """,
            nativeQuery = true)
    List<CommentItem> findCommentsByPostId(Long postId, Long lastCommentId);

    default CommentEntity findByIdOrElseThrow(Long commentId) {
        return findById(commentId).orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "댓글을 찾을 수 없습니다."));
    }
}
