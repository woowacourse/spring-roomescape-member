package roomescape.reservation.domain.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Theme;

@Repository
public class JdbcThemeDao implements ThemeRepository {

    private final RowMapper<Theme> rowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String thumbnail = rs.getString("thumbnail");
        return new Theme(id, name, description, thumbnail);
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveAndReturnId(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, theme.getName());
            preparedStatement.setString(2, theme.getDescription());
            preparedStatement.setString(3, theme.getThumbnail());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    @Override
    public List<Theme> findByPeriodAndLimit(LocalDate start, LocalDate end, int limit) {
        String sql = """
                SELECT th.id, th.name, th.description, th.thumbnail, r.theme_count FROM theme AS th
                JOIN
                (
                    SELECT COUNT(theme_id) AS theme_count, theme_id FROM reservation
                    WHERE date BETWEEN ? AND ?
                    GROUP BY theme_id
                ) AS r
                ON th.id = r.theme_id
                ORDER BY theme_count DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, rowMapper, start, end, limit);
    }
}
