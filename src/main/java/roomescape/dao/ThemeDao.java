package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeDao {

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> {
        return new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail"));
    };

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Long saveTheme(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        return jdbcTemplate.query(sql, themeRowMapper, id).stream().findFirst();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean isDuplicatedNameExisted(String name) {
        String sql = "SELECT EXISTS (SELECT id, name, description, thumbnail FROM theme WHERE name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, name));
    }

    public List<Theme> findThemesByReservationVolumeBetweenDates(LocalDate baseDate, int dayRange, int limit) {
        LocalDate startDate = baseDate.minusDays(dayRange);

        String sql = """
                    SELECT theme.id AS id,
                           theme.name AS name,
                           theme.description AS description,
                           theme.thumbnail AS thumbnail,
                           COUNT(reservation.id) AS reservation_count
                    FROM theme
                    INNER JOIN reservation ON theme.id = reservation.theme_id
                    WHERE reservation.date < ?
                      AND reservation.date >= ?
                    GROUP BY theme.id
                    ORDER BY reservation_count DESC
                    LIMIT ?
                """;

        return jdbcTemplate.query(sql, themeRowMapper,
                baseDate.toString(),
                startDate.toString(),
                limit);
    }
}
