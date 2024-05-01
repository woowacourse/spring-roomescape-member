package roomescape.theme.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.theme.model.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> themeRowMapper =
            (resultSet, rowNum) -> new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    @Override
    public List<Theme> findAll() {
        String sql = "select id, name, description, thumbnail from theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }
}
