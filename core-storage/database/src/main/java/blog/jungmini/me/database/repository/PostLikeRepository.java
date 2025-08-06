package blog.jungmini.me.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.PostLikeEntity;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    Optional<PostLikeEntity> findByPostIdAndUserId(Long postId, Long userId);

    default PostLikeEntity findByPostIdAndUserIdOrElseThrow(Long postId, Long userId) {
        return findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "좋아요 데이터를 찾을 수 없습니다."));
    }
}
