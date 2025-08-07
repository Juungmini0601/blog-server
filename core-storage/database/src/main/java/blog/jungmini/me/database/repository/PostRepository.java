package blog.jungmini.me.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.PostEntity;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long>, PostRepositoryCustom {

    default PostEntity findByIdOrElseThrow(Long postId) {
        return findById(postId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.VALIDATION_ERROR, String.format("게시글을 찾을 수 없습니다 ID: [%d]", postId)));
    }
}
