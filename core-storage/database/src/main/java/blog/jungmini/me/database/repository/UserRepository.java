package blog.jungmini.me.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    default UserEntity findByIdOrElseThrow(Long userId) {
        return findById(userId)
                .orElseThrow(() ->
                        new CustomException(ErrorType.VALIDATION_ERROR, String.format("존재하지 않는 유저 아이디: [%d]", userId)));
    }
}
