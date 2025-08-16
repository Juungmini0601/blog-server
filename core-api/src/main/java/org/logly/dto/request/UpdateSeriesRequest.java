package org.logly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;

import org.logly.database.entity.SeriesEntity;

@Getter
public class UpdateSeriesRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    public UpdateSeriesRequest() {}

    public UpdateSeriesRequest(String name) {
        this.name = name;
    }

    public SeriesEntity toEntity() {
        return SeriesEntity.builder().name(name).build();
    }
}
