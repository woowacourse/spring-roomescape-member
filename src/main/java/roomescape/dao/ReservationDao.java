package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            LocalDate.parse(resultSet.getString("date")),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("start_at"))
            ),
            resultSet.getLong("theme_id")
    );

    public List<Reservation> findAllReservations() {
        String sql = """
                SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at,
                    r.theme_id
                FROM reservation as r
                INNER JOIN reservation_time as t
                  ON r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation findReservationById(Long id) {
        String sql = """
                SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at,
                    r.theme_id
                FROM reservation as r
                INNER JOIN reservation_time as t
                  ON r.time_id = t.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
    }

    public Long insertReservation(String name, LocalDate date, Long timeId, Long themeId) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            ps.setString(1, name);
            ps.setString(2, date.toString());
            ps.setLong(3, timeId);
            ps.setLong(4, themeId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
