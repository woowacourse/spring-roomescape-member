package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.util.List;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> rowMapper;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }

    public List<Theme> readThemes() {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

}
