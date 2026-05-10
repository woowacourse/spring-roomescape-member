package roomescape.user.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.admin.domain.Theme;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> Theme.of(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("description"),
        resultSet.getString("image_url")
    );

    public Theme findById(Long id) {
        String query = "select * from theme where id = ?";
        return jdbcTemplate.queryForObject(query, rowMapper, id);
    }
}
