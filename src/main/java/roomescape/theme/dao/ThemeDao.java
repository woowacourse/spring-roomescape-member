package roomescape.theme.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.Dao;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeDao implements Dao<Theme> {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ThemeDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Theme add(Theme theme) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());
        long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme";
        return namedParameterJdbcTemplate.query(sql,
                (resultSet, rowNum) -> createTheme(resultSet));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        Map<String, Object> parameter = Map.of("id", id);
        namedParameterJdbcTemplate.update(sql, parameter);
    }

    private Theme createTheme(ResultSet resultSet) throws SQLException {
        return new roomescape.theme.domain.Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
