package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO theme(name, description, thumbnail) VALUES(?, ?, ?)",
                            new String[]{"id"});
                    ps.setString(1, theme.getName());
                    ps.setString(2, theme.getDescription());
                    ps.setString(3, theme.getThumbnail());
                    return ps;
                }
                , keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(
                "DELETE FROM theme WHERE id = ?",
                id
        );
        return deletedRow == 1;
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, description, thumbnail"
                        + " FROM theme",
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }

    public Optional<Theme> findById(Long id) {
        try {
            String sql = """
                    SELECT id, name, description, thumbnail
                    FROM THEME
                    WHERE id = ?
                    """;
            Theme theme = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            ), id);
            return Optional.ofNullable(theme);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> getTopTenTheme() {
        String sql = """
                SELECT id, name, description, thumbnail FROM THEME AS t
                INNER JOIN
                (SELECT THEME_ID, count(THEME_ID) AS COUNT
                FROM RESERVATION
                GROUP BY THEME_ID
                ORDER BY COUNT DESC, THEME_ID ASC
                LIMIT 10) AS popular
                ON t.ID = popular.THEME_ID
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail")
        ));
    }

    public boolean isExistThemeName(String name) {
        String sql = """
                SELECT EXISTS (SELECT 1 FROM theme WHERE name = ?)
                """;
        return jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                name
        );
    }
}
