package roomescape.theme.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeRepository {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ThemeRepository(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Theme add(Theme theme) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", theme.name());
        parameters.put("description", theme.description());
        parameters.put("thumbnail", theme.thumbnail());

        long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Theme(
                id,
                theme.name(),
                theme.description(),
                theme.thumbnail());
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail "
                + "FROM theme "
                + "WHERE id = :id";

        Map<String, Object> parameter = Map.of("id", id);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(
                    sql, parameter,
                    (resultSet, rowNum) -> createTheme(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> findTop10MostReservedLastWeek(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT t.id, t.name, t.description, t.thumbnail "
                + "FROM theme AS t "
                + "INNER JOIN reservation AS r ON r.theme_id = t.id "
                + "WHERE r.date BETWEEN :startDate AND :endDate "
                + "GROUP BY t.id, t.name, t.description, t.thumbnail "
                + "ORDER BY COUNT(r.id) DESC "
                + "LIMIT 10";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);

        return namedParameterJdbcTemplate.query(sql, parameters,
                (resultSet, rowNum) -> createTheme(resultSet));
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme";

        return namedParameterJdbcTemplate.query(sql,
                (resultSet, rowNum) -> createTheme(resultSet));
    }

    public boolean existsByName(String name) {
        String sql = "SELECT EXISTS (SELECT 1 FROM theme WHERE name = :name)";

        Map<String, Object> parameter = Map.of("name", name);

        return Boolean.TRUE.equals(
                namedParameterJdbcTemplate.queryForObject(sql, parameter, Boolean.class));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";

        Map<String, Object> parameter = Map.of("id", id);

        namedParameterJdbcTemplate.update(sql, parameter);
    }

    private Theme createTheme(ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
