package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.dto.ThemeRequestDto;
import roomescape.model.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Theme addTheme(ThemeRequestDto themeRequestDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into theme(name, description, thumbnail) VALUES (?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, themeRequestDto.name());
            ps.setString(2, themeRequestDto.description());
            ps.setString(3, themeRequestDto.thumbnail());
            return ps;
        }, keyHolder);
        return new Theme(Objects.requireNonNull(keyHolder.getKey().longValue()), themeRequestDto.name(), themeRequestDto.description(), themeRequestDto.thumbnail());
    }
}
