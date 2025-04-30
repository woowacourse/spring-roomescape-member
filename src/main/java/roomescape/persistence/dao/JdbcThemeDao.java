package roomescape.persistence.dao;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.Theme;
import roomescape.persistence.entity.ThemeEntity;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final Theme theme) {
        final ThemeEntity themeEntity = ThemeEntity.from(theme);
        final String sql = "INSERT INTO THEME (name, description, thumbnail) values (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, themeEntity.name());
            ps.setString(2, themeEntity.description());
            ps.setString(3, themeEntity.thumbnail());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
