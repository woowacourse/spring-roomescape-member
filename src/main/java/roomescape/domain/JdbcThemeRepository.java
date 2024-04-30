package roomescape.domain;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT id, name, description, thumbnail FROM theme", getThemeRowMapper());
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail"));
    }
}
