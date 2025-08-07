package blog.jungmini.me.database.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
