package roomescape.domain.reservation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ThemeRepository;

@Repository
public class ThemeDAO implements ThemeRepository {

    private static final String TABLE_NAME = "theme";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeDAO(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> themeOf(resultSet));
    }

    private Theme themeOf(ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }

    @Override
    public Theme save(Theme theme) {
        if (theme.existId()) {
            return update(theme);
        }
        return create(theme);
    }

    private Theme update(Theme theme) {
        String sql = """
                update theme 
                set name = :name, description = :description, thumbnail = :thumbnail
                where id = :id
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("name", theme.getName());
        params.put("description", theme.getDescription());
        params.put("thumbnail", theme.getThumbnail());

        int updateRowCount = jdbcTemplate.update(sql, params);

        if (updateRowCount == 0) {
            throw new EntityNotFoundException("ReservationTime with id " + theme.getId() + " not found");
        }

        return theme;
    }

    private Theme create(Theme theme) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public void deleteById(Long id) {
        String deleteSql = "delete from theme where id = :id";
        Map<String, Long> params = Map.of("id", id);

        int deleteRowCount = jdbcTemplate.update(deleteSql, params);

        if (deleteRowCount != 1) {
            throw new EntityNotFoundException("ReservationTime with id " + id + " not found");
        }
    }
}
