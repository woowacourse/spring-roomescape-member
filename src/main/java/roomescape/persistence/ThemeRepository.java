package roomescape.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeRepository {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme create(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public boolean removeById(Long id) {
        int updatedRowCount = jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
        return updatedRowCount == 1;
    }

    public List<Theme> findOrderedByReservationCountInPeriod(LocalDate startDate, LocalDate endDate,
                                                             int limitCount) {
        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail
                FROM theme as t
                LEFT JOIN reservation AS r
                ON t.id = r.theme_id
                AND convert(r.date, DATE) BETWEEN ? AND ?
                GROUP BY t.id
                ORDER BY count(r.id) DESC, t.id ASC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, THEME_ROW_MAPPER, startDate, endDate, limitCount);
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT id, name, description, thumbnail FROM theme", THEME_ROW_MAPPER);
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, THEME_ROW_MAPPER, id));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
