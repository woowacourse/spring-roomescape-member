package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("time_id"), resultSet.getTime("time_value").toLocalTime()),
            new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"), resultSet.getString("theme_thumbnail"))
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = jdbcTemplate.query("""
                SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at as time_value,
                th.id as theme_id,
                th.name as theme_name,
                th.description as theme_description,
                th.thumbnail as theme_thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                INNER JOIN theme as th on r.theme_id = th.id
                """, reservationRowMapper);
        return Collections.unmodifiableList(reservations);
    }

    private Reservation findById(final long id) {
        String sql = """
                SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at as time_value,
                th.id as theme_id,
                th.name as theme_name,
                th.description as theme_description,
                th.thumbnail as theme_thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                INNER JOIN theme as th on r.theme_id = th.id
                WHERE r.id = ?
                """;

        return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
    }

    public boolean existByDateAndTimeAndTheme(LocalDate date, LocalTime time, Long themeId) {
        int count = jdbcTemplate.queryForObject("""
                SELECT count(*) 
                FROM reservation as r 
                INNER JOIN reservation_time as t ON r.time_id = t.id
                WHERE r.date = ? AND t.start_at = ? AND r.theme_id = ?
                """, Integer.class, date, time, themeId);
        return count > 0;
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(reservation);
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameterSource);
        return findById(newId.longValue());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Long> findTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        return jdbcTemplate.queryForList("SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?", Long.class,
                date, themeId);
    }
}
