package roomescape.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.ReservationTime;

@Component
public class ReservationTimeRowMapper implements RowMapper<ReservationTime> {

    @Override
    public ReservationTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReservationTime(
                rs.getLong("id"),
                LocalTime.parse(rs.getString("start_at"))
        );
    }
}
