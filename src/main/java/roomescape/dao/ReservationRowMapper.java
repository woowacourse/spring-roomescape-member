package roomescape.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Component
public class ReservationRowMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        long reservationId = rs.getLong("reservation_id");
        String name = rs.getString("name");
        String date = rs.getString("date");
        long timeId = rs.getLong("time_id");
        String startAt = rs.getString("time_value");
        return new Reservation(reservationId, name, date, ReservationTime.from(timeId, startAt));
    }
}
