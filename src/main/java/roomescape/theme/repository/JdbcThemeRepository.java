package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme(name, description, thumbnail) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement psmt = con.prepareStatement(sql, new String[]{"id"});
            psmt.setString(1, theme.getName());
            psmt.setString(2, theme.getDescription());
            psmt.setString(3, theme.getThumbnail());

            return psmt;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        List<Theme> themes = jdbcTemplate.query(sql, themeRowMapper, id);
        return themes.stream().findFirst();
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Theme> findPopularThemes(LocalDate start, LocalDate end) {

        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail
                FROM theme t
                INNER JOIN reservation r ON r.theme_id = t.id
                WHERE date BETWEEN ? AND ?
                GROUP BY t.id, t.name, t.description, t.thumbnail
                ORDER BY COUNT(*) DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(sql, themeRowMapper, start, end);
    }
}
