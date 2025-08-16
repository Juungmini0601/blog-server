package org.logly.database.repository;

import java.util.Optional;

import org.logly.database.projection.PostDetail;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

public interface PostRepositoryCustom {
    void setSeriesIdNullBySeriesId(Long seriesId);

    Optional<PostDetail> findPostDetailById(Long postId);

    default PostDetail findPostDetailByIdOrElseThrow(Long postId) {
        return findPostDetailById(postId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.VALIDATION_ERROR, String.format("게시글을 찾을 수 없습니다 ID: [%d]", postId)));
    }
}
