package org.logly.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.logly.database.entity.SeriesEntity;
import org.logly.database.entity.UserEntity;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

@Repository
public interface SeriesRepository extends CrudRepository<SeriesEntity, Long>, SeriesRepositoryCustom {
    default SeriesEntity findByIdOrElseThrow(Long seriesId) {
        return findById(seriesId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.VALIDATION_ERROR, String.format("존재하지 않는 시리즈 아이디: [%d]", seriesId)));
    }

    List<SeriesEntity> findAllByUser(UserEntity user);
}
