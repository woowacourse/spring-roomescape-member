package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcTemplateThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query("select ID, NAME, DESCRIPTION, THUMBNAIL from THEME", (rs, rowNum) -> {
            long id = rs.getLong(1);
            String name = rs.getString(2);
            String description = rs.getString(3);
            String thumbnail = rs.getString(4);
            return new Theme(id, name, description, thumbnail);
        });
    }

    @Override
    public Optional<Theme> findById(long id) {
        List<Theme> themes = jdbcTemplate.query("select id, name, description, thumbnail from theme where id = ?",
                (rs, rowNum) -> {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String thumbnail = rs.getString("thumbnail");
                    return new Theme(id, name, description, thumbnail);
                }, id);
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
