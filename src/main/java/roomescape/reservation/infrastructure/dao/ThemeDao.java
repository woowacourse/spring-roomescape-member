package roomescape.reservation.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.presentation.dto.ThemeRequest;

@Repository
public class ThemeDao implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(ThemeRequest themeRequest) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("theme")
                .usingColumns("name", "description", "thumbnail")
                .usingGeneratedKeyColumns("id");

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", themeRequest.getName())
                .addValue("description", themeRequest.getDescription())
                .addValue("thumbnail", themeRequest.getThumbnail());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }
}
