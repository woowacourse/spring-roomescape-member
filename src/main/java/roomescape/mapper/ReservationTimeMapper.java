package roomescape.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import roomescape.entity.ReservationTime;

public class ReservationTimeMapper implements RowMapper<ReservationTime> {

    @Override
    public ReservationTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReservationTime(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        );
    }
}
