package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> Theme.of(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("image_url")
    );

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme save(Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("image_url", theme.getImageUrl());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Theme.of(id, theme.getName(), theme.getDescription(), theme.getImageUrl());
    }

    public Optional<Theme> findById(Long id) {
        String query = "select * from theme where id = ?";
        return jdbcTemplate.query(query, rowMapper, id).stream().findFirst();
    }

    public List<Theme> findAll() {
        String query = "select * from theme";
        return jdbcTemplate.query(query, rowMapper);
    }

    public void deleteById(Long id) {
        String query = "delete from theme where id = ?";
        jdbcTemplate.update(query, id);
    }

    public List<Long> findThemeIdTop10(LocalDate startDate, LocalDate endDate) {
        String query = """
            SELECT r.theme_id
            FROM reservation r
            WHERE r.date BETWEEN ? AND ?
            GROUP BY r.theme_id
            ORDER BY COUNT(r.id) DESC
            LIMIT 10
            """;
        return jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("theme_id"), startDate, endDate);
    }

    public List<Theme> findAllByIds(List<Long> ids) {
        String placeholders = ids.stream().map(id -> "?").collect(Collectors.joining(", "));
        String query = "SELECT id, name, description, image_url FROM theme WHERE id IN (" + placeholders + ")";
        return jdbcTemplate.query(query, rowMapper, ids.toArray());
    }
}