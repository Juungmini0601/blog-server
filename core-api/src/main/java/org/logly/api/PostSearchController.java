package org.logly.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.logly.application.PostSearchService;
import org.logly.database.projection.PostItem;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.response.CursorResponse;

@RestController
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService postSearchService;

    @GetMapping("/v1/posts/search")
    public CursorResponse<PostItem, Long> search(
            @RequestParam String keyword, @RequestParam(required = false) Long lastPostId) {
        if (keyword.isBlank() || keyword.length() < 2) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "검색은 2글자 이상만 가능합니다.");
        }

        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        return postSearchService.search(keyword, lastPostId);
    }
}
