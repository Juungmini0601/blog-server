package org.logly.database.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import org.logly.database.projection.PostDetail;

@Repository
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PostRepositoryCustomImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setSeriesIdNullBySeriesId(Long seriesId) {
        String sql = "UPDATE posts SET series_id = NULL WHERE series_id = :seriesId";

        Map<String, Object> params = new HashMap<>();
        params.put("seriesId", seriesId);

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Optional<PostDetail> findPostDetailById(Long postId) {
        String sql =
                """
            SELECT p.post_id , p.user_id, u.nickname AS user_nickname, u.profile_image_url AS user_profile_image_url, u.introduction AS user_introduction, p.title, p.content, p.thumbnail_url, p.is_public, p.series_id, p.created_at, p.updated_at,
                (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.post_id) AS like_count
            FROM posts p
            JOIN users u ON p.user_id = u.user_id
            WHERE p.post_id = :postId AND p.is_public = true
            """;

        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);

        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(PostDetail.class)));
    }
}
