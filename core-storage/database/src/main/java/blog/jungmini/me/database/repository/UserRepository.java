package blog.jungmini.me.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.projection.UserFollowItem;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(
            """
        SELECT user_id, nickname, profile_image_url, introduction
        FROM users
        WHERE user_id IN (:userIds)
    """)
    List<UserFollowItem> findUserFollowItemsByIds(List<Long> userIds);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    default UserEntity findByIdOrElseThrow(Long userId) {
        return findById(userId)
                .orElseThrow(() ->
                        new CustomException(ErrorType.VALIDATION_ERROR, String.format("존재하지 않는 유저 아이디: [%d]", userId)));
    }
}
