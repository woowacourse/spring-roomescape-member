package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.SearchRequest;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Theme> themeRowMapper = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail_url")
    );

    private final RowMapper<AvailableReservationTimeResponse> availableReservationTimeRowMapper =
            (rs, rowNum) -> new AvailableReservationTimeResponse(
                    rs.getLong("id"),
                    rs.getString("start_at"),
                    rs.getBoolean("available")
            );

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<Theme> findById(Long id) {
        try {
            Theme theme = jdbcTemplate.queryForObject(
                    "SELECT id, name, description, thumbnail_url FROM theme WHERE id = ?",
                    themeRowMapper, id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> findAll() {
        try {
            return jdbcTemplate.query( "SELECT id, name, description, thumbnail_url FROM theme", themeRowMapper);
        } catch (EmptyResultDataAccessException ignored) {
        }

        return List.of();
    }

    public List<Theme> findPopularThemes(int size, LocalDate from, LocalDate to) {
        try {
            final String sql = """
                            SELECT
                                th.id,
                                th.name,
                                th.description,
                                th.thumbnail_url
                            FROM reservation AS r
                            INNER JOIN theme AS th ON r.theme_id = th.id
                            WHERE r.date BETWEEN ? AND ?
                            GROUP BY
                                th.id,
                                th.name,
                                th.description,
                                th.thumbnail_url
                            ORDER BY COUNT(r.id) DESC
                            LIMIT ?
                            """;
            return jdbcTemplate.query(sql, themeRowMapper, from, to, size);
        } catch (EmptyResultDataAccessException ignored) {
        }

        return List.of();
    }


    public List<AvailableReservationTimeResponse> findAvailableTimeById(long themeId, String date) {
        try {
            final String sql = """
                    SELECT                                                                                                                                                                                                         \s
                          rt.id,                                                                                                                                                                                                     \s
                          rt.start_at,                                                                                                                                                                                               \s
                          CASE WHEN r.id IS NULL THEN TRUE ELSE FALSE END AS available                                                                                                                                               \s
                      FROM reservation_time rt                                                                                                                                                                                       \s
                      LEFT JOIN reservation r                                                                                                                                                                                      \s
                          ON rt.id = r.time_id
                           AND r.theme_id = ?
                           AND r.date = ?
                    """;
            return jdbcTemplate.query(sql, availableReservationTimeRowMapper, themeId, date);
        } catch (EmptyResultDataAccessException ignored) {
        }
        return List.of();
    }

    public Long save(String name, String description, String thumbnailUrl) {
        return jdbcInsert.executeAndReturnKey(Map.of(
                "name", name,
                "description", description,
                "thumbnailUrl", thumbnailUrl
        )).longValue();
    }


    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }
}
