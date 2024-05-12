package roomescape.repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
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

    public Optional<Theme> findById(Long id) {
        String query = "SELECT * FROM THEME WHERE ID = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(query, themeRowMapper, id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Theme save(Theme theme) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(theme);
        long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Theme(id, theme.name(), theme.description(), theme.thumbnail());
    }

    public void delete(long themeID) {
        String query = "DELETE FROM THEME WHERE ID = ?";
        jdbcTemplate.update(query, themeID);
    }

    public List<Theme> getLastWeekTop10(LocalDate currentDate) {
        LocalDate weekAgo = currentDate.minusWeeks(1);
        Timestamp currentTimestamp = Timestamp.valueOf(currentDate.atStartOfDay());
        Timestamp weekAgoTimestamp = Timestamp.valueOf(weekAgo.atStartOfDay());
        String query = "SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count " +
                "FROM theme t " +
                "INNER JOIN reservation r ON t.id = r.theme_id " +
                "WHERE r.date >= ? " +
                "AND r.date < ? " +
                "GROUP BY t.id, t.name, t.description, t.thumbnail " +
                "ORDER BY reservation_count DESC " +
                "LIMIT 10";
        return jdbcTemplate.query(query, themeRowMapper, weekAgoTimestamp, currentTimestamp);
    }

    public boolean existsById(Long id) {
        String query = "SELECT EXISTS (SELECT 1 FROM THEME WHERE ID = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, id));
    }

    public boolean existsByName(String name) {
        String query = "SELECT EXISTS (SELECT 1 FROM THEME WHERE name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, name));
    }
}
