package roomescape.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeDAO implements ThemeDAO {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (resultSet, rowNumber) ->
            new Theme(resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Theme> findAll() {
        String query = "SELECT * FROM theme";
        return jdbcTemplate.query(query, THEME_ROW_MAPPER);
    }
}
