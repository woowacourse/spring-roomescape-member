package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> getPopularThemeByRankAndDuration(int rank, LocalDate startAt, LocalDate endAt) {
        String sql = """
                SELECT id, name, description, thumbnail
                            FROM (
                                SELECT t.id, t.name, t.description, t.thumbnail,
                                       RANK() OVER (ORDER BY COUNT(r.THEME_ID) DESC, r.THEME_ID ASC) as theme_rank
                                FROM THEME t
                                LEFT JOIN RESERVATION r ON t.ID = r.THEME_ID
                                WHERE r.date BETWEEN ? AND ?
                                GROUP BY t.id, t.name, t.description, t.thumbnail
                            ) ranked
                            WHERE theme_rank <= ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                ),
                startAt,
                endAt,
                rank
        );
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

    public int countTotalTheme() {
        String sql = """
                SELECT COUNT(*) FROM THEME
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<Theme> findThemesWithPage(int startRowNumber, int endRowNumber) {
        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail
                    FROM (
                        SELECT ROW_NUMBER() OVER() as row_num, * 
                        FROM THEME
                    ) as t
                WHERE t.row_num BETWEEN ? AND ?
                ORDER BY t.row_num
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                ),
                startRowNumber,
                endRowNumber
        );
    }

    public List<Theme> findAll() {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }
}
