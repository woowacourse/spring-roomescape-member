package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

import java.util.List;

@Repository
public class ThemeDAO implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAllThemes() {
        String sql = "select id, name, description, thumbnail from theme";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ));
    }

    @Override
    public Theme addTheme(Theme theme) {
        return null;
    }

    @Override
    public long deleteTheme(long id) {
        return 0;
    }
}
