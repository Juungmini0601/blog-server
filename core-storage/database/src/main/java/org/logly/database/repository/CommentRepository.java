package org.logly.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.logly.database.entity.CommentEntity;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long>, CommentRepositoryCustom {

    default CommentEntity findByIdOrElseThrow(Long commentId) {
        return findById(commentId).orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "댓글을 찾을 수 없습니다."));
    }
}
