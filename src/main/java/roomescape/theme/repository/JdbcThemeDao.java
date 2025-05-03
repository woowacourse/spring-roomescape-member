package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcThemeDao implements ThemeRepository {

    private final RowMapper<Theme> rowMapper = (rs, rowNum) ->
            new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            );

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
        List<Theme> findThemes = jdbcTemplate.query(sql, rowMapper, id);
        if (findThemes.isEmpty()) {
            return Optional.empty();
        }
        if (findThemes.size() > 1) {
            throw new IllegalStateException("조회 결과가 2개 이상입니다.");
        }
        return Optional.of(findThemes.getFirst());
    }

    @Override
    public List<Long> findTopThemeIdByDateRange(LocalDate start, LocalDate end, int limit) {
        String sql = """
                SELECT theme_id
                FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ORDER BY COUNT(theme_id) DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("theme_id"), start, end, limit);
    }

    @Override
    public List<Theme> findByIdIn(List<Long> ids) {
        if (ids.isEmpty()) {
            throw new IllegalStateException("테마 ID가 존재하지 않습니다.");
        }
        String holders = String.join(", ", Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id IN (" + holders + ")";
        return jdbcTemplate.query(sql, ids.toArray(), rowMapper);
    }
}
