package org.logly.database.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import org.logly.database.entity.PostLikeEntity;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

public interface PostLikeRepository extends CrudRepository<PostLikeEntity, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    Optional<PostLikeEntity> findByPostIdAndUserId(Long postId, Long userId);

    default PostLikeEntity findByPostIdAndUserIdOrElseThrow(Long postId, Long userId) {
        return findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "좋아요 데이터를 찾을 수 없습니다."));
    }
}
