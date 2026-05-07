package roomescape.repository.theme;

import java.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
        new Theme(
            rs.getLong("id"),
            new ThemeName(rs.getString("name")),
            rs.getString("description"),
            new ThemeImageUrl(rs.getString("image_url")));
    
    private final JdbcTemplate template;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }

    @Override
    public Theme createTheme(Theme theme) {
        String sql = """
            INSERT INTO theme(name, description, image_url)
            VALUES (?, ?, ?);
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getNameValue());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getImageUrlValue());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return theme.withId(id);
    }

    @Override
    public void deleteById(Long id) {
        int update = template.update("""
            DELETE FROM theme
            WHERE id = ?;
            """, id);

        // TODO: NoSuchElementException 쓰는게 맞는지?
        if (update == 0) {
            throw new NoSuchElementException("존재하지 않는 theme 의 id 입니다. id = " + id);
        }
    }

    @Override
    public List<Theme> findAll() {
        return template.query("""
            SELECT id, name, description, image_url
            FROM theme;
            """,
            THEME_ROW_MAPPER);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        List<Theme> themes = template.query("""
                SELECT id, name, description, image_url
                FROM theme
                WHERE id = ?;
                """,
            THEME_ROW_MAPPER,
            id);

        return themes.stream().findFirst();
    }

    @Override
    public List<Theme> findWeekPopularThemesOrderByRank(final int limit) {
        final String sql = """
            SELECT t.id, t.name, t.description, t.image_url
            FROM theme t
            JOIN reservation r ON r.theme_id = t.id
            WHERE r.res_date >= ? AND r.res_date <= ?
            GROUP BY t.id
            ORDER BY COUNT(r.id) DESC
            LIMIT ?
            """;

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        return template.query(sql, THEME_ROW_MAPPER, sevenDaysAgo.toString(), yesterday.toString(), limit);
    }
}
