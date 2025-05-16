package roomescape.domain.theme.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.model.Theme;

@Repository
@Primary
public class JdbcThemeDaoImpl implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcThemeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("theme")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        String query = "select * from theme";
        return jdbcTemplate.query(query,
            (resultSet, RowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
            ));
    }

    @Override
    public long save(Theme theme) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean delete(Long id) {
        String query = "delete from theme where id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String query = "select * from theme where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> calculateRankForReservationAmount(LocalDate startDate,
        LocalDate currentDate) {
        String query = """
            SELECT id, name, description, thumbnail
            FROM
            (
                SELECT theme_id, COUNT(*) AS reservation_count
                FROM reservation
                WHERE date >= ? AND date <= ?
                GROUP BY theme_id
            ) AS sub
            INNER JOIN theme ON sub.theme_id = theme.id;     
            """;

        return jdbcTemplate.query(query,
            (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
            ), startDate, currentDate);
    }
}
