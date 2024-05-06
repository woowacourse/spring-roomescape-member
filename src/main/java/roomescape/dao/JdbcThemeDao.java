package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
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
                (resultSet, rowNum) -> getTheme(resultSet)
        );
    }

    @Override
    public Optional<Theme> readById(Long id) {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                WHERE id = ?
                """;
        List<Theme> themes = jdbcTemplate.query(sql, (resultSet, rowNum) -> getTheme(resultSet), id);
        if (themes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(themes.get(0));
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
                    ps.setString(1, theme.getName().getValue());
                    ps.setString(2, theme.getDescription().getValue());
                    ps.setString(3, theme.getThumbnail().getValue());
                    return ps;
                },
                keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean exist(long id) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM theme WHERE id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, boolean.class, id);
    }

    @Override
    public boolean exist(String name) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM theme WHERE name = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, boolean.class, name);
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

    private Theme getTheme(ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("id"),
                ThemeName.from(resultSet.getString("name")),
                ThemeDescription.from(resultSet.getString("description")),
                ThemeThumbnail.from(resultSet.getString("thumbnail"))
        );
    }
}
