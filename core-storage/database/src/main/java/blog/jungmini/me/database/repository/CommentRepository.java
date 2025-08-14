package blog.jungmini.me.database.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.CommentEntity;
import blog.jungmini.me.database.projection.CommentItem;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    @Query(
            """
            SELECT c.comment_id, c.post_id, c.content, c.created_at, c.user_id, u.nickname, u.profile_image_url
            FROM comments c
            JOIN users u ON c.user_id = u.user_id
            WHERE c.post_id = :postId AND c.comment_id < :lastCommentId
            ORDER BY c.comment_id DESC
            LIMIT 20
            """)
    List<CommentItem> findCommentsByPostId(Long postId, Long lastCommentId);

    default CommentEntity findByIdOrElseThrow(Long commentId) {
        return findById(commentId).orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "댓글을 찾을 수 없습니다."));
    }
}
