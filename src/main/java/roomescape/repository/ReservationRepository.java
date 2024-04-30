package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("reservation_time_id"),
                    resultSet.getTime("time_value").toLocalTime())
    );

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existByReservationTimeId(Long reservationTimeId) {
        String sql = "SELECT exists(SELECT 1 FROM reservation " +
                "where reservation_time_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationTimeId);
    }

    public List<Reservation> findAll() {
        String sql = "SELECT " +
                "    r.id as reservation_id, " +
                "    r.name, " +
                "    r.date, " +
                "    t.id as reservation_time_id, " +
                "    t.start_at as time_value " +
                "FROM reservation as r " +
                "inner join reservation_time as t " +
                "on r.reservation_time_id = t.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, reservation_time_id) values (?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getReservationTime().getId());
            return ps;
        }, keyHolder);

        return new Reservation(keyHolder.getKey().longValue(), reservation.getName(),
                reservation.getDate(), reservation.getReservationTime());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            return ps;
        });
    }

    public Optional<Reservation> findByDateAndTime(LocalDate date, ReservationTime reservationTime) {
        String sql = "SELECT id, name, date, reservation_time_id FROM reservation " +
                "where date = ? and reservation_time_id = ?";

        return jdbcTemplate.query(sql,
                        (resultSet, rowNum) -> new Reservation(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getDate("date").toLocalDate(),
                                reservationTime
                        ), Date.valueOf(date),
                        reservationTime.getId()).stream()
                .findAny();
    }
}
