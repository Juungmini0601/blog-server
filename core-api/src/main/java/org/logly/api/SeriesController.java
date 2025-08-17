package org.logly.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import org.logly.application.SeriesService;
import org.logly.database.entity.SeriesEntity;
import org.logly.dto.request.CreateSeriesRequest;
import org.logly.dto.request.UpdateSeriesRequest;
import org.logly.dto.response.CreateSeriesResponse;
import org.logly.dto.response.SeriesItemResponse;
import org.logly.dto.response.UpdateSeriesResponse;
import org.logly.response.ApiResponse;
import org.logly.security.model.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping("/v1/series/{userId}")
    public ApiResponse<List<SeriesItemResponse>> getSeriesList(@PathVariable("userId") Long userId) {
        return ApiResponse.success(seriesService.getSeriesItemsByUserId(userId).stream()
                .map(SeriesItemResponse::fromEntity)
                .toList());
    }

    @PostMapping("/v1/series")
    public ApiResponse<CreateSeriesResponse> create(
            Authentication authentication, @Valid @RequestBody CreateSeriesRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        SeriesEntity createdSeries = seriesService.create(details, request);

        return ApiResponse.success(CreateSeriesResponse.fromEntity(createdSeries));
    }

    @PutMapping("/v1/series/{seriesId}")
    public ApiResponse<UpdateSeriesResponse> update(
            Authentication authentication,
            @PathVariable Long seriesId,
            @Valid @RequestBody UpdateSeriesRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        SeriesEntity updatedSeries = seriesService.update(seriesId, details, request);

        return ApiResponse.success(UpdateSeriesResponse.fromEntity(updatedSeries));
    }

    @DeleteMapping("/v1/series/{seriesId}")
    public ApiResponse<?> delete(Authentication authentication, @PathVariable Long seriesId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        seriesService.remove(seriesId, details);

        return ApiResponse.success();
    }
}
