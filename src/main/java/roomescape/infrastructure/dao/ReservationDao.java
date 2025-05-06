package roomescape.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.model.Reservation;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String query = """
                   SELECT r.id, r.name, r.date,
                   rt.id AS time_id, rt.start_at AS time_value,
                   t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, t.thumbnail AS theme_thumbnail
                   FROM reservation AS r
                   INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                   INNER JOIN theme AS t ON r.theme_id = t.id
                """;
        return jdbcTemplate.query(
                query,
                ROW_MAPPER
        );
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public boolean existByTimeId(Long timeId) {
        String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, timeId);
    }

    public boolean existByThemeId(Long themeId) {
        String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, themeId);
    }

    public boolean existByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date) {
        String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ? and theme_id = ? and date = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, timeId, themeId, date);
    }

    public List<Long> findBookedTimes(final Long themeId, final LocalDate date) {
        String query = "SELECT time_id FROM reservation WHERE theme_id = ? and date = ?";
        return jdbcTemplate.query(query, (resultSet, row) -> resultSet.getLong(1), themeId, date);
    }
}
