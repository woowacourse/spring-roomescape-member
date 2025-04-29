package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.dto.ReservationRequestDto;
import roomescape.model.Reservation;
import roomescape.model.ReservationDate;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.UserName;

@Repository
public class JdbcReservationRepository implements ReservationRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> getAllReservations() {
        String sql = "SELECT r.id, r.name, r.date, r.time_id, t.start_at FROM reservation as r inner join reservation_time as t on r.time_id = t.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Reservation(
                rs.getLong("id"),
                new UserName(rs.getString("name")),
                new ReservationDateTime(
                        new ReservationDate(LocalDate.parse(rs.getString("date"))),
                        new ReservationTime(rs.getLong("time_id"), rs.getTime("start_at").toLocalTime())
                )
        ));
    }

    @Override
    public Reservation addReservation(ReservationRequestDto reservationRequestDto, ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (name, date, time_id) values (?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, new String[]{"id"});
            ps.setString(1, reservationRequestDto.name());
            ps.setString(2, reservationRequestDto.date().toString());
            ps.setLong(3, reservationRequestDto.timeId());
            return ps;
        }, keyHolder);
        return reservationRequestDto.toEntity(Objects.requireNonNull(keyHolder.getKey()).longValue(), reservationTime);
    }

    @Override
    public void deleteReservation(Long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }
}
