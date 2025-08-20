package org.logly.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.logly.database.entity.UserEntity;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

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
