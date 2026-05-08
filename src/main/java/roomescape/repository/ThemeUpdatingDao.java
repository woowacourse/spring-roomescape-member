package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.ThemeRequest;

@Repository
public class ThemeUpdatingDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeUpdatingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(ThemeRequest themeRequest) {
        String sql = "INSERT INTO theme (name, description, url) VALUES (:name, :description, :url);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", themeRequest.getName())
                .addValue("description", themeRequest.getDescription())
                .addValue("url", themeRequest.getUrl());
        jdbcTemplate.update(sql, param, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.update(sql, param);
    }
}
