package org.logly.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.logly.database.entity.PostEntity;
import org.logly.database.projection.PostItem;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long>, PostRepositoryCustom {
    // TODO 테이블 설계 변경 예정, 현재 시스템은 쿼리가 너무 많이 날라감
    @Query(
            value =
                    """
            SELECT p.post_id, p.thumbnail_url, p.created_at, p.content, u.user_id, u.nickname, u.profile_image_url,
               (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.post_id) AS comment_count,
               (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.post_id) AS like_count
            FROM posts p
                JOIN users u ON p.user_id = u.user_id
            WHERE p.post_id < :lastPostId
            ORDER BY p.post_id DESC
            LIMIT 20
            """,
            nativeQuery = true)
    List<PostItem> findPosts(Long lastPostId);

    default PostEntity findByIdOrElseThrow(Long postId) {
        return findById(postId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.VALIDATION_ERROR, String.format("게시글을 찾을 수 없습니다 ID: [%d]", postId)));
    }
}
