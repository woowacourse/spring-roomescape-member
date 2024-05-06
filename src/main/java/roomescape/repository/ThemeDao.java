package roomescape.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.domain.Theme;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Theme> themeRowMapper = (resultSet, __) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT * FROM THEME", themeRowMapper);
    }

    public Theme findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM THEME WHERE ID = ?", themeRowMapper, id);
    }

    public Theme save(Theme theme) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(theme);
        long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return findById(id);
    }

    public void delete(long themeID) {
        String query = "DELETE FROM THEME WHERE ID = ?";
        jdbcTemplate.update(query, themeID);
    }

    public List<Theme> getTop10() {
        String query = "SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count " +
                "FROM theme t " +
                "INNER JOIN reservation r ON t.id = r.theme_id " +
                "WHERE r.date >=( TIMESTAMPADD(DAY, -7, CURRENT_DATE)) " +
                "AND r.date <= ( TIMESTAMPADD(DAY, -1, CURRENT_DATE)) " +
                "GROUP BY t.id, t.name, t.description, t.thumbnail " +
                "ORDER BY reservation_count DESC " +
                "LIMIT 10";
        return jdbcTemplate.query(query, themeRowMapper);
    }

    public boolean existsById(Long id) {
        String query = "SELECT COUNT(*) FROM THEME WHERE ID = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByName(String name) {
        String query = "SELECT COUNT(*) FROM THEME WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, name);
        return count != null && count > 0;
    }
}
