package blog.jungmini.me.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import blog.jungmini.me.application.ImageService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.dto.response.ImageUploadResponse;

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
