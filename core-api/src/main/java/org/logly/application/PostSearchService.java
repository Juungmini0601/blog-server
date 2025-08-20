package org.logly.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.logly.database.projection.PostItem;
import org.logly.database.repository.PostSearchRepository;
import org.logly.redis.service.RedisService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final PostSearchRepository postSearchRepository;
    private final RedisService redisService;
    private static final String SLOW_KEYWORD_PREFIX = "slow_keywords:";
    private static final long SLOW_QUERY_TIME = 1000L;
    private static final long CACHE_TTL_SECOND = 3600L;

    @SuppressWarnings("unchecked")
    public List<PostItem> search(String keyword) {
        Optional<List> cachedResult = redisService.get(SLOW_KEYWORD_PREFIX + keyword, List.class);
        if (cachedResult.isPresent()) {
            return (List<PostItem>) cachedResult.get();
        }

        long startTime = System.currentTimeMillis();
        List<PostItem> result = postSearchRepository.search(keyword);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        if (executionTime > SLOW_QUERY_TIME) {
            log.warn("Slow search detected. keyword: {}, executionTime: {}ms", keyword, executionTime);
            redisService.set(SLOW_KEYWORD_PREFIX + keyword, result, CACHE_TTL_SECOND);
            log.info("Slow search cache saved. keyword: {}", keyword);
        }

        return result;
    }
}
