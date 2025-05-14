package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.RoomTheme;

@Repository
public class JdbcRoomThemeRepository implements RoomThemeRepository {

    private static final RowMapper<RoomTheme> THEME_ROW_MAPPER = (resultSet, rowNumber) ->
            new RoomTheme(resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcRoomThemeRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("theme_id");
    }

    @Override
    public long insert(final RoomTheme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("theme_name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        final Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean existsByName(String name) {
        final String query = "SELECT EXISTS (SELECT 1 FROM theme WHERE theme_name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, name));
    }

    @Override
    public List<RoomTheme> findAll() {
        final String query = "SELECT * FROM theme";
        return jdbcTemplate.query(query, THEME_ROW_MAPPER);
    }

    @Override
    public Optional<RoomTheme> findById(final long id) {
        final String query = "SELECT * FROM theme WHERE theme_id = ?";
        return jdbcTemplate.query(query, THEME_ROW_MAPPER, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<RoomTheme> findPopularThemes(LocalDate start, LocalDate end, int topLimit) {

        final String query = """
                        SELECT
                            t.theme_id,
                            theme_name,
                            description,
                            thumbnail
                        FROM theme AS t
                        JOIN reservation AS r
                        ON r.theme_id = t.theme_id
                        WHERE r.date BETWEEN ? AND ?
                        GROUP BY t.theme_id
                        ORDER BY COUNT(r.reservation_id) DESC
                        LIMIT ?
                """;
        return jdbcTemplate.query(query, THEME_ROW_MAPPER, start, end, topLimit);
    }

    @Override
    public boolean deleteById(long id) {
        final String query = "DELETE FROM theme WHERE theme_id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }
}
