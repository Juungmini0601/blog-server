package org.logly.database.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.database.projection.PostItem;

@Repository
@RequiredArgsConstructor
public class PostSearchRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<PostItem> search(String keyword) {
        String sql =
                """
                SELECT
                    p.post_id AS postId,
                    p.thumbnail_url AS thumbnailUrl,
                    p.created_at AS createdAt,
                    p.content,
                    p.user_id AS userId,
                    u.nickname,
                    u.profile_image_url AS profileImageUrl,
                    p.like_count,
                    p.comment_count
                FROM logly.posts p JOIN logly.users u ON p.user_id = u.user_id
                WHERE p.search_vector @@ to_tsquery('public.korean', :keyword)
                ORDER BY p.post_id DESC
                LIMIT 400
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("keyword", keyword);

        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(PostItem.class));
    }
}
