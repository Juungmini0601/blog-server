package org.logly.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.logly.database.entity.SeriesEntity;
import org.logly.database.repository.PostRepository;
import org.logly.database.repository.SeriesRepository;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

@Service
public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final PostRepository postRepository;

    public SeriesService(SeriesRepository seriesRepository, PostRepository postRepository) {
        this.seriesRepository = seriesRepository;
        this.postRepository = postRepository;
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

    @Transactional
    public void remove(Long userId, Long seriesId) {
        SeriesEntity findedSeries = seriesRepository.findByIdOrElseThrow(seriesId);

        if (!findedSeries.isOwner(userId)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "시리즈 작성자만 삭제 할 수 있습니다.");
        }

        postRepository.setSeriesIdNullBySeriesId(seriesId);
        seriesRepository.deleteById(seriesId);
    }
}
