package blog.jungmini.me.database.repository;

import java.util.Optional;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.projection.PostDetail;

public interface PostRepositoryCustom {
    void setSeriesIdNullBySeriesId(Long seriesId);

    Optional<PostDetail> findPostDetailById(Long postId);

    default PostDetail findPostDetailByIdOrElseThrow(Long postId) {
        return findPostDetailById(postId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.VALIDATION_ERROR, String.format("게시글을 찾을 수 없습니다 ID: [%d]", postId)));
    }
}
