package roomescape.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.condition.ThemeInsertCondition;
import roomescape.domain.ReservationTheme;

@Repository
public class ReservationThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public ReservationThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTheme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, getReservationThemeRowMapper());
    }

    public Optional<ReservationTheme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        List<ReservationTheme> reservationThemes = jdbcTemplate.query(sql, getReservationThemeRowMapper(), id);

        return Optional.ofNullable(DataAccessUtils.singleResult(reservationThemes));
    }

    public ReservationTheme insert(ThemeInsertCondition insertCondition) {
        String name = insertCondition.getName();
        String description = insertCondition.getDescription();
        String thumbnail = insertCondition.getThumbnail();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("thumbnail", thumbnail);

        Long id = insertActor.executeAndReturnKey(parameters).longValue();

        return new ReservationTheme(id, name, description, thumbnail);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<ReservationTheme> getReservationThemeRowMapper() {
        return (resultSet, numRow) -> new ReservationTheme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
