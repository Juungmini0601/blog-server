package org.logly.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.logly.database.entity.SeriesEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.projection.SeriesItem;
import org.logly.database.repository.PostRepository;
import org.logly.database.repository.SeriesRepository;
import org.logly.database.repository.UserRepository;
import org.logly.dto.request.CreateSeriesRequest;
import org.logly.dto.request.UpdateSeriesRequest;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.security.model.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SeriesEntity> getSeriesItemsByUserId(Long userId) {
        UserEntity user = userRepository.findByIdOrElseThrow(userId);
        return seriesRepository.findAllByUser(user);
    }

    @Transactional
    public SeriesEntity create(CustomUserDetails userDetails, CreateSeriesRequest request) {
        UserEntity requester = userRepository.findByIdOrElseThrow(userDetails.getUserId());
        SeriesEntity series =
                SeriesEntity.builder().name(request.getName()).user(requester).build();
        return seriesRepository.save(series);
    }

    @Transactional
    public SeriesEntity update(Long seriesId, CustomUserDetails userDetails, UpdateSeriesRequest request) {
        UserEntity requester = userRepository.findByIdOrElseThrow(userDetails.getUserId());
        SeriesEntity findedSeries = seriesRepository.findByIdOrElseThrow(seriesId);

        if (!findedSeries.getUser().equals(requester)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "시리즈 작성자만 수정 할 수 있습니다.");
        }

        findedSeries.setName(request.getName());
        return seriesRepository.save(findedSeries);
    }

    @Transactional
    public void remove(Long seriesId, CustomUserDetails userDetails) {
        UserEntity requester = userRepository.findByIdOrElseThrow(userDetails.getUserId());
        SeriesEntity findedSeries = seriesRepository.findByIdOrElseThrow(seriesId);

        if (!findedSeries.getUser().equals(requester)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "시리즈 작성자만 삭제 할 수 있습니다.");
        }

        postRepository.setSeriesIdNullBySeries(findedSeries);
        seriesRepository.deleteById(seriesId);
    }
}
