package roomescape.repository.theme;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate template;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }

    @Override
    public Theme createTheme(Theme theme) {
        String sql = "INSERT INTO theme(name, description, image_url) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getImageUrl());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return theme.withId(id);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?;";
        int update = template.update(sql, id);

        if (update == 0) {
            throw new NoSuchElementException("존재하지 않는 theme 의 id 입니다. id = " + id);
        }
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, image_url FROM theme;";

        return template.query(sql, themeRowMapper());
    }

    private RowMapper<Theme> themeRowMapper() {
        return (rs, rowNum) ->
                new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image_url"));
    }
}
