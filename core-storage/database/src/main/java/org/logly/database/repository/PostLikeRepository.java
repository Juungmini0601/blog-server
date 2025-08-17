package org.logly.database.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import org.logly.database.entity.PostEntity;
import org.logly.database.entity.PostLikeEntity;
import org.logly.database.entity.UserEntity;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

public interface PostLikeRepository extends CrudRepository<PostLikeEntity, Long> {
    boolean existsByPostAndUser(PostEntity post, UserEntity user);

    Optional<PostLikeEntity> findByPostAndUser(PostEntity post, UserEntity user);

    default PostLikeEntity findByPostIdAndUserIdOrElseThrow(PostEntity post, UserEntity user) {
        return findByPostAndUser(post, user)
                .orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "좋아요 데이터를 찾을 수 없습니다."));
    }
}
