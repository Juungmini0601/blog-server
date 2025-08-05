package blog.jungmini.me.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.SeriesEntity;
import blog.jungmini.me.database.repository.SeriesRepository;

@Service
public class SeriesService {
    private final SeriesRepository seriesRepository;

    public SeriesService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @Transactional
    public SeriesEntity create(SeriesEntity series) {
        return seriesRepository.save(series);
    }

    @Transactional
    public SeriesEntity update(SeriesEntity series) {
        SeriesEntity findedSeries = seriesRepository.findByIdOrElseThrow(series.getSeriesId());

        if (!findedSeries.isOwner(series.getUserId())) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "시리즈 작성자만 수정 할 수 있습니다.");
        }

        findedSeries.setName(series.getName());
        return seriesRepository.save(findedSeries);
    }
}
