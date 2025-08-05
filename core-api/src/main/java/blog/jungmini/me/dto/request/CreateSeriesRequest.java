package blog.jungmini.me.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;

import blog.jungmini.me.database.entity.SeriesEntity;

@Getter
public class CreateSeriesRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    public CreateSeriesRequest() {}

    public CreateSeriesRequest(String name) {
        this.name = name;
    }

    public SeriesEntity toEntity() {
        return SeriesEntity.builder().name(name).build();
    }
}
