package blog.jungmini.me.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    default CommentEntity findByIdOrElseThrow(Long commentId) {
        return findById(commentId).orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "댓글을 찾을 수 없습니다."));
    }
}
