package roomescape.reservation.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                """
                SELECT r.id, r.name, r.date, r.time_id, rt.start_time, rt.end_time
                FROM reservation r
                LEFT JOIN reservation_time rt ON r.time_id = rt.id
                """,
                new ReservationRowMapper()
        );
    }

    @Override
    public Reservation save(Reservation reservation) {
        Number id = reservationInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId()));
        return reservation.withId(id.longValue());
    }

    @Override
    public List<Long> findTimeIdsByDate(LocalDate date) {
        return jdbcTemplate.query(
                "SELECT time_id FROM reservation WHERE date = ?",
                (rs, rowNum) -> rs.getLong("time_id"),
                Date.valueOf(date)
        );
    }

    @Override
    public boolean deleteById(Long id) {
        int affectedRows = jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
        return affectedRows > 0;
    }

    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReservationTime time = null;
            Long timeId = rs.getLong("time_id");
            if (!rs.wasNull()) {
                time = new ReservationTime(timeId, rs.getString("start_time"), rs.getString("end_time"));
            }
            Reservation reservation = new Reservation(
                    rs.getString("name"),
                    rs.getDate("date").toLocalDate(),
                    time
            );
            return reservation.withId(rs.getLong("id"));
        }
    }
}

