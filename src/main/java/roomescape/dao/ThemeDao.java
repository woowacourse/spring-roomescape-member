package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import roomescape.domain.Theme;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> rowMapper;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }

    public List<Theme> readThemes() {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Theme> readThemeById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Theme> readThemesRankingOfReservation(String startDate, String endDate) {
        String sql = """
                SELECT theme.id, theme.name, theme.description, theme.thumbnail, COUNT(reservation.theme_id) AS reservation_count
                FROM theme
                JOIN reservation
                ON reservation.theme_id = theme.id
                WHERE reservation.date >= ? AND reservation.date <= ?
                GROUP BY theme.id, theme.name, theme.description, theme.thumbnail
                ORDER BY reservation_count DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(sql, rowMapper, startDate, endDate);
    }

    public boolean existsThemeByName(String name) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM theme
                    WHERE name = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    public Theme createTheme(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, theme.getName());
            preparedStatement.setString(2, theme.getDescription());
            preparedStatement.setString(3, theme.thumbnail());
            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return theme.createWithId(id);
    }

    public void deleteTheme(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
