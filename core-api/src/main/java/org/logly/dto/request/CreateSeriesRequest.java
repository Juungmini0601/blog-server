package org.logly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateSeriesRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;
}
