package org.logly.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.logly.application.ImageService;
import org.logly.dto.response.ImageUploadResponse;
import org.logly.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/api/v1/images/upload-url")
    public ApiResponse<ImageUploadResponse> getPresignedUrl(@RequestParam String filename) {
        ImageUploadResponse uploadInfo = imageService.generatePresignedUploadUrl(filename);
        return ApiResponse.success(uploadInfo);
    }
}
