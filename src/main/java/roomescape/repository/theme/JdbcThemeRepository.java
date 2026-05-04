package roomescape.repository.theme;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;

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
}
