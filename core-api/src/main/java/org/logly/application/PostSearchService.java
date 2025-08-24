package org.logly.application;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.QueryTimeoutException;
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

    /**
     * @param keyword 검색 키워드
     *                2vCPU, 4MEM 인스턴스 기준으로 400만 레코드 풀 스캔 실행 결과 2500ms ~ 3000ms 확인
     *                전문검색 인덱스 적용시 레코드 풀 스캔 수행시간 1500ms ~ 2500ms 확인 (조건 절 검사, 페이징을 안하면 성능은 훨씬 좋음 300ms)
     *                레코드를 풀 스캔 하는 경우는 검색 결과 매칭이 거의 안되거나 아주 예전 데이터에 있는 경우
     *                이 기능에서 테이블 풀 스캔을 하는 경우의 Spike성 트래픽이 한 번에 몰리는 경우에는 DB CPU 폭증이 일어날 가능성이 있음
     *                서버 안정성을 위해서 Rate Limit 적용이 필요함, 자주 검색 되는 키워드 캐싱
     */
    @SuppressWarnings("unchecked")
    public CursorResponse<PostItem, Long> search(String keyword, Long lastPostId) {
        try {
            List<PostItem> result = postRepository.searchPosts(keyword, lastPostId);

            if (result.isEmpty()) {
                return CursorResponse.of(result, null, false);
            }

            Long nextCursor = result.get(result.size() - 1).getPostId();
            boolean hasNext = result.size() == 20;
            return CursorResponse.of(result, nextCursor, hasNext);
        } catch (QueryTimeoutException e) {
            log.warn("QueryTimeoutException: keyword: {}, lastPostId: {}", keyword, lastPostId);
            return CursorResponse.of(Collections.emptyList(), null, false);
        }
    }
}
