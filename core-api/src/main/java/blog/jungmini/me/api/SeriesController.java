package blog.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.jungmini.me.application.SeriesService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.SeriesEntity;
import blog.jungmini.me.dto.request.CreateSeriesRequest;
import blog.jungmini.me.dto.response.CreateSeriesResponse;
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
}
