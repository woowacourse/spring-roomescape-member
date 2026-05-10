package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ThemeQueryingDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ThemeQueryingDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> {
        Theme theme = new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("url")
        );
        return theme;
    };

    public Optional<Theme> findThemeById(long id) {
        String sql = """
                SELECT id, name, description, url
                FROM theme
                WHERE id = :id
                """;

        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            Theme theme = jdbcTemplate.queryForObject(sql, param, themeRowMapper);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<Theme> findAllTheme() {
        String sql = """
                SELECT id, name, description, url
                FROM theme
                """;
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public List<Theme> findAllByTopTheme(Integer period, Integer limit) {
        String sql = """
                SELECT t.id, t.name, t.description, t.url
                FROM theme as t
                INNER JOIN (
                    SELECT r.theme_id
                    FROM reservation as r
                    WHERE r.date >= :filtered AND r.date < :today
                    GROUP BY r.theme_id
                    ORDER BY count(1) DESC, r.theme_id ASC
                    LIMIT :limit
                ) AS top_themes ON t.id = top_themes.theme_id;
                """;

        LocalDate today = LocalDate.now();
        LocalDate filtered = today.minusDays(period);

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("filtered", filtered)
                .addValue("today", today);

        return jdbcTemplate.query(sql, param, themeRowMapper);
    }
}
