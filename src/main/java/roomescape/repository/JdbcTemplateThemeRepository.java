package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.Themes;

@Repository
public class JdbcTemplateThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> themeRowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String thumbnail = rs.getString("thumbnail");
        return new Theme(id, name, description, thumbnail);
    };

    public JdbcTemplateThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Themes findAll() {
        List<Theme> findThemes = jdbcTemplate.query(
                "SELECT id, name, description, thumbnail FROM THEME", themeRowMapper
        );
        return new Themes(findThemes);
    }

    @Override
    public Themes findAndOrderByPopularity(LocalDate start, LocalDate end, int count) {
        List<Theme> findThemes = jdbcTemplate.query(
                """
                        SELECT TH.*, COUNT(*) AS count FROM THEME TH
                            JOIN RESERVATION R
                            ON R.theme_id = TH.id
                        WHERE PARSEDATETIME(R.date,'yyyy-MM-dd') >= PARSEDATETIME(?,'yyyy-MM-dd') 
                            AND PARSEDATETIME(R.date,'yyyy-MM-dd') <= PARSEDATETIME(?,'yyyy-MM-dd') 
                        GROUP BY TH.id 
                        ORDER BY count 
                        DESC 
                        LIMIT ?
                        """,
                themeRowMapper, start, end, count);
        return new Themes(findThemes);
    }

    @Override
    public Optional<Theme> findById(long id) {
        List<Theme> themes = jdbcTemplate.query(
                "SELECT id, name, description, thumbnail FROM THEME WHERE id = ?",
                themeRowMapper, id);
        return themes.stream().findFirst();
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        save(theme, keyHolder);
        long id = keyHolder.getKey().longValue();
        return new Theme(id, theme);
    }

    private void save(Theme theme, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
                    PreparedStatement pstm = con.prepareStatement(
                            "INSERT INTO THEME (name, description, thumbnail) VALUES (?, ?, ?) ",
                            new String[]{"id"}
                    );
                    pstm.setString(1, theme.getName());
                    pstm.setString(2, theme.getDescription());
                    pstm.setString(3, theme.getThumbnail());
                    return pstm;
                },
                keyHolder);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM THEME WHERE id = ?", id);
    }
}
