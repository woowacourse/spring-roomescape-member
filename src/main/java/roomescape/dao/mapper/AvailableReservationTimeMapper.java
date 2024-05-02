package roomescape.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.dao.dto.AvailableReservationTimeResponse;


@Component
public class AvailableReservationTimeMapper implements RowMapper<AvailableReservationTimeResponse> {

    @Override
    public AvailableReservationTimeResponse mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new AvailableReservationTimeResponse(
                rs.getBoolean("booked"),
                rs.getLong("time_id"),
                rs.getString("start_at")
        );
    }
}
