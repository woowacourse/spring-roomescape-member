package roomescape.dao;

import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Primary
@Repository
public class JdbcThemeDao implements ThemeDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme read(Long id) {
        String sql = "SELECT * FROM `theme` WHERE `id` = id";

        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNumber) -> {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String thumbnailUrl = resultSet.getString("thumbnail_url");
                    return new Theme(id, name, description, thumbnailUrl);
                }
        );
    }

    @Override
    public List<Theme> readAll() {
        String sql = "SELECT * FROM `theme`";

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) -> {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String thumbnailUrl = resultSet.getString("thumbnail_url");
                    return new Theme(id, name, description, thumbnailUrl);
                }
        );
    }
}
