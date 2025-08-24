package org.logly.api;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.logly.application.PostSearchService;
import org.logly.database.projection.PostItem;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.redis.service.RedisService;
import org.logly.response.CursorResponse;

@RestController
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService postSearchService;
    private final RedisService redisService;

    @GetMapping("/v1/posts/search")
    public CursorResponse<PostItem, Long> search(
            @RequestParam String keyword, @RequestParam(required = false) Long lastPostId) {
        if (keyword.isBlank() || keyword.length() < 2) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "검색은 2글자 이상만 가능합니다.");
        }

        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        String cacheKey = "search:" + keyword + ":" + lastPostId;

        Optional<List<PostItem>> cachedResult = redisService.getList(cacheKey, PostItem.class);
        if (cachedResult.isPresent()) {
            List<PostItem> result = cachedResult.get();
            redisService.set(cacheKey, result, 30);

            if (result.isEmpty()) {
                return CursorResponse.of(result, null, false);
            }

            Long nextCursor = result.get(result.size() - 1).getPostId();
            boolean hasNext = result.size() == 20;
            return CursorResponse.of(result, nextCursor, hasNext);
        }

        boolean allowed = redisService.tryAcquireSemaphore("semaphore:search", 50, 5);
        if (!allowed) {
            throw new CustomException(ErrorType.TOO_MANY_REQUEST);
        }

        try {
            CursorResponse<PostItem, Long> result = postSearchService.search(keyword, lastPostId);
            redisService.set(cacheKey, result.getData(), 30);
            return result;
        } finally {
            redisService.releaseSemaphore("semaphore:search");
        }
    }
}
