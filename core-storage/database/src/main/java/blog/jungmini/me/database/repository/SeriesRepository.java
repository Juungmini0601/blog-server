package blog.jungmini.me.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.SeriesEntity;

@Repository
public interface SeriesRepository extends JpaRepository<SeriesEntity, Long> {
    default SeriesEntity findByIdOrElseThrow(Long seriesId) {
        return findById(seriesId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.VALIDATION_ERROR, String.format("존재하지 않는 시리즈 아이디: [%d]", seriesId)));
    }
}
