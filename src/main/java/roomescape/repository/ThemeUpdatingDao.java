package roomescape.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.dto.ThemeCreateRequest;

@Repository
public class ThemeUpdatingDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ThemeUpdatingDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(ThemeCreateRequest themeRequest) {
        String sql = "INSERT INTO theme (name, description, url) VALUES (:name, :description, :url);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", themeRequest.getName())
                .addValue("description", themeRequest.getDescription())
                .addValue("url", themeRequest.getUrl());
        jdbcTemplate.update(sql, param, keyHolder, new String[]{"id"});

        return keyHolder.getKey().longValue();
    }

    public void delete(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(sql, param);
    }
}
