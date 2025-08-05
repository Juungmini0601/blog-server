package blog.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import blog.jungmini.me.application.SeriesService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.SeriesEntity;
import blog.jungmini.me.dto.request.CreateSeriesRequest;
import blog.jungmini.me.dto.request.UpdateSeriesRequest;
import blog.jungmini.me.dto.response.CreateSeriesResponse;
import blog.jungmini.me.dto.response.UpdateSeriesResponse;
import blog.jungmini.me.security.model.CustomUserDetails;

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
