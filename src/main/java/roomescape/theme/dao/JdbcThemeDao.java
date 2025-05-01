package roomescape.theme.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme create(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail"))
        );
    }

    @Override
    public int delete(long id) {
        String sql = "delete from theme where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Theme> findByName(String name) {
        String sql = "select name from theme where name = ?";

        try {
            Theme theme = this.jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> {
                        Theme foundTheme = new Theme(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("thumbnail")
                        );
                        return foundTheme;
                    }, name
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select * from theme where id = ?";

        try {
            Theme theme = this.jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> {
                        Theme foundTheme = new Theme(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("thumbnail")
                        );
                        return foundTheme;
                    }, id
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findTopNReservedThemesBetween(int count, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT *
                FROM (SELECT theme_id, COUNT(*) AS count
                FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ) AS r
                RIGHT JOIN theme AS t ON r.theme_id = t.id
                ORDER BY count DESC, name
                LIMIT ?
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                ),
                startDate, endDate, count

        );
    }
}
