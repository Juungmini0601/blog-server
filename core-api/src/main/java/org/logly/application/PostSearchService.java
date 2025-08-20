package org.logly.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.logly.database.projection.PostItem;
import org.logly.database.repository.PostRepository;
import org.logly.redis.service.RedisService;
import org.logly.response.CursorResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final PostRepository postRepository;
    private final RedisService redisService;
    private static final String SLOW_KEYWORD_PREFIX = "slow_keywords:";
    private static final long SLOW_QUERY_TIME = 1000L;
    private static final long CACHE_TTL_SECOND = 3600L;

    /**
     * @param keyword 검색 키워드
     * 2vCPU, 4MEM 인스턴스 기준으로 400만 레코드 풀 스캔 실행 결과 2500ms ~ 3000ms 확인
     * 전문검색 인덱스 적용시 레코드 풀 스캔 수행시간 1500ms ~ 2500ms 확인
     * 레코드를 풀 스캔 하는 경우는 검색 결과 매칭이 거의 안되거나 아주 예전 데이터에 있는 경우,
     */
    @SuppressWarnings("unchecked")
    public CursorResponse<PostItem, Long> search(String keyword, Long lastPostId) {
        Optional<List<PostItem>> cachedResult = redisService.getList(SLOW_KEYWORD_PREFIX + keyword, PostItem.class);
        if (cachedResult.isPresent()) {
            List<PostItem> result = cachedResult.get();

            if (result.isEmpty()) {
                return CursorResponse.of(result, null, false);
            }

            Long nextCursor = result.get(result.size() - 1).getPostId();
            boolean hasNext = result.size() == 20;
            return CursorResponse.of(result, nextCursor, hasNext);
        }

        long startTime = System.currentTimeMillis();
        List<PostItem> result = postRepository.searchPosts(keyword, lastPostId);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        if (executionTime > SLOW_QUERY_TIME) {
            log.warn("Slow search detected. keyword: {}, executionTime: {}ms", keyword, executionTime);
            redisService.set(SLOW_KEYWORD_PREFIX + keyword, result, CACHE_TTL_SECOND);
            log.info("Slow search cache saved. keyword: {}", keyword);
        }

        if (result.isEmpty()) {
            return CursorResponse.of(result, null, false);
        }

        Long nextCursor = result.get(result.size() - 1).getPostId();
        boolean hasNext = result.size() == 20;
        return CursorResponse.of(result, nextCursor, hasNext);
    }
}
