package org.logly.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.logly.application.SeriesService;
import org.logly.database.entity.SeriesEntity;
import org.logly.dto.request.CreateSeriesRequest;
import org.logly.dto.request.UpdateSeriesRequest;
import org.logly.dto.response.CreateSeriesResponse;
import org.logly.dto.response.UpdateSeriesResponse;
import org.logly.response.ApiResponse;
import org.logly.security.model.CustomUserDetails;

@RestController
public class SeriesController {

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PostMapping("/v1/series")
    public ApiResponse<CreateSeriesResponse> create(
            Authentication authentication, @Valid @RequestBody CreateSeriesRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        SeriesEntity seriesEntity = SeriesEntity.builder()
                .userId(details.getUserId())
                .name(request.getName())
                .build();

        SeriesEntity createdSeries = seriesService.create(seriesEntity);
        return ApiResponse.success(CreateSeriesResponse.fromEntity(createdSeries));
    }

    @PutMapping("/v1/series/{seriesId}")
    public ApiResponse<UpdateSeriesResponse> update(
            Authentication authentication,
            @PathVariable Long seriesId,
            @Valid @RequestBody UpdateSeriesRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        SeriesEntity series = SeriesEntity.builder()
                .seriesId(seriesId)
                .userId(details.getUserId())
                .name(request.getName())
                .build();
        SeriesEntity updatedSeries = seriesService.update(series);

        return ApiResponse.success(UpdateSeriesResponse.fromEntity(updatedSeries));
    }

    @DeleteMapping("/v1/series/{seriesId}")
    public ApiResponse<?> delete(Authentication authentication, @PathVariable Long seriesId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        seriesService.remove(details.getUserId(), seriesId);

        return ApiResponse.success();
    }
}
