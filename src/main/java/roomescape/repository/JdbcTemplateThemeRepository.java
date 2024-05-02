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
    public List<Theme> findAll() {
        return jdbcTemplate.query("select ID, NAME, DESCRIPTION, THUMBNAIL from THEME", themeRowMapper);
    }

    @Override
    public List<Theme> findAndOrderByPopularity(LocalDate start, LocalDate end, int count) {
        return jdbcTemplate.query(
                "select th.*, count(*) as count from theme th join reservation r on r.theme_id = th.id where PARSEDATETIME(r.date,'yyyy-MM-dd') >= PARSEDATETIME(?,'yyyy-MM-dd') and PARSEDATETIME(r.date,'yyyy-MM-dd') <= PARSEDATETIME(?,'yyyy-MM-dd') group by th.id order by count desc limit ?",
                themeRowMapper, start, end, count);
    }

    @Override
    public Optional<Theme> findById(long id) {
        List<Theme> themes = jdbcTemplate.query("select id, name, description, thumbnail from theme where id = ?",
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
                            "insert into THEME (NAME, DESCRIPTION, THUMBNAIL) values (?, ?, ?) ", new String[]{"id"});
                    pstm.setString(1, theme.getName());
                    pstm.setString(2, theme.getDescription());
                    pstm.setString(3, theme.getThumbnail());
                    return pstm;
                },
                keyHolder);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from THEME where id = ?", id);
    }
}
