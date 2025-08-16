package org.logly.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageUploadResponse {
    private final String uploadUrl;
    private final String imageKey;
    private final String accessUrl;
}
