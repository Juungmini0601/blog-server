package blog.jungmini.me.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
