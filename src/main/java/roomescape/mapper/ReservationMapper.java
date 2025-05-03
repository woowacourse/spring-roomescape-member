package roomescape.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class ReservationMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Reservation(
                rs.getLong("id"),
                rs.getString("reservation_name"),
                rs.getObject("date", LocalDate.class),
                new ReservationTime(
                        rs.getLong("time_id"),
                        rs.getObject("start_at", LocalTime.class)
                ),
                new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }
}
