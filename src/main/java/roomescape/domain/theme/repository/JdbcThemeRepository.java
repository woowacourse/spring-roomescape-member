package roomescape.domain.theme.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.entity.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAllThemes() {
        String sql = "SELECT id, name, description, image_url FROM themes";
        return jdbcTemplate.query(
            sql,
            (resultSet, rowNum) -> Theme.reconstruct(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("imageUrl")
            ));
    }
}
