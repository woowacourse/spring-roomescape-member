package roomescape.theme.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.global.exception.InfrastructureException;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private static final String THEME_CREATE_FAILED_MESSAGE = "테마 생성에 실패했습니다.";

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowCount = insert(theme, keyHolder);
        validateCreatedRowCount(rowCount);

        Long id = getGeneratedId(keyHolder);
        return theme.withId(id);
    }

    @Override
    public List<Theme> findAll() {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;

        return jdbcTemplate.query(sql, themeRowMapper);
    }

    @Override
    public List<Theme> findTopThemesByReservationCount(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.thumbnail
                FROM theme t
                INNER JOIN reservation r
                    ON r.theme_id = t.id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id, t.name, t.description, t.thumbnail
                ORDER BY COUNT(r.id) DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, themeRowMapper, startDate, endDate, limit);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, themeRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM theme
                    WHERE id = ?
                )
                """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    @Override
    public boolean existsByName(String name) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM theme
                    WHERE name = ?
                )
                """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, name));
    }

    @Override
    public void deleteById(Long id) {
        String sql = """
                DELETE FROM theme
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    private int insert(Theme theme, KeyHolder keyHolder) {
        String sql = """
                INSERT INTO theme (name, description, thumbnail)
                VALUES (?, ?, ?)
                """;

        return jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, theme.getName());
            preparedStatement.setString(2, theme.getDescription());
            preparedStatement.setString(3, theme.getThumbnail());
            return preparedStatement;
        }, keyHolder);
    }

    private void validateCreatedRowCount(int rowCount) {
        if (rowCount != 1) {
            throw new InfrastructureException(THEME_CREATE_FAILED_MESSAGE);
        }
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new InfrastructureException(THEME_CREATE_FAILED_MESSAGE);
        }
        return key.longValue();
    }
}
