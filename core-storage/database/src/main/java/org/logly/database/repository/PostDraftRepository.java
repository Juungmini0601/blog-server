package org.logly.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.logly.database.entity.PostDraftEntity;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

@Repository
public interface PostDraftRepository extends JpaRepository<PostDraftEntity, Long>, PostDraftRepositoryCustom {
    default PostDraftEntity findByIdOrElseThrow(Long postDraftId) {
        return findById(postDraftId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.VALIDATION_ERROR, String.format("존재하지 않는 초안 아이디: [%d]", postDraftId)));
    }
}
