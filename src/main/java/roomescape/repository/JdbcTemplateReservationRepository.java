package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;

@Repository
public class JdbcTemplateReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findReservationTime(reservationRequest);
        Reservation reservation = new Reservation(null, reservationRequest.name(), reservationRequest.date(),
                reservationTime);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        save(reservation, keyHolder);
        long id = keyHolder.getKey().longValue();
        return new Reservation(id, reservation);
    }

    private ReservationTime findReservationTime(ReservationRequest reservationRequest) {
        String reservationTimeSelectSql = "select * from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(reservationTimeSelectSql, (rs, rowNum) -> {
            long id = rs.getLong(1);
            LocalTime startAt = rs.getTime(2).toLocalTime();
            return new ReservationTime(id, startAt);
        }, reservationRequest.timeId());
    }

    private void save(Reservation reservation, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
            String sql = "insert into reservation(name,date,time_id) values ( ?,?,? )";
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate()));
            preparedStatement.setLong(3, reservation.getReservationTime().getId());
            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public List<Reservation> findAll() {
        String query = "SELECT "
                + "    r.id as reservation_id,"
                + "    r.name,"
                + "    r.date,"
                + "    t.id as time_id,"
                + "    t.start_at as time_value"
                + " FROM reservation as r"
                + " inner join reservation_time as t"
                + " on r.time_id = t.id";
        return jdbcTemplate.query(query,
                (rs, rowNum) -> {
                    long id = rs.getLong(1);
                    String name = rs.getString(2);
                    LocalDate date = rs.getDate(3).toLocalDate();
                    long timeId = rs.getLong(4);
                    LocalTime startAt = rs.getTime(5).toLocalTime();
                    ReservationTime reservationTime = new ReservationTime(timeId, startAt);
                    return new Reservation(id, name, date, reservationTime);
                });
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }
}
