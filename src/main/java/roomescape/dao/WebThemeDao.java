package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;

@Repository
public class WebThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public WebThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> readAll() {
        String sql = """
                SELECT
                id, name, description, thumbnail
                FROM theme
                """;
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        );
    }

    @Override
    public Theme create(Theme theme) {
        String sql = """
                INSERT
                INTO theme
                    (name, description, thumbnail)
                VALUES
                    (?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, theme.getName());
                    ps.setString(2, theme.getDescription());
                    ps.setString(3, theme.getThumbnail());
                    return ps;
                },
                keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public Boolean exist(long id) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM theme WHERE id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public void delete(long id) {
        String sql = """
                DELETE
                FROM theme
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
