package roomescape.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;

public class ReservationMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Reservation(
                new Id(rs.getLong("id")),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                new ReservationTime(
                        new Id(rs.getLong("time_id")),
                        rs.getObject("start_at", LocalTime.class)
                )
        );
    }
}
