package roomescape.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.AvailableReservationTime;


@Component
public class AvailableReservationTimeMapper implements RowMapper<AvailableReservationTime> {

    @Override
    public AvailableReservationTime mapRow(final ResultSet rs, final int rowNum) throws SQLException {

        return new AvailableReservationTime(
                rs.getBoolean("booked"),
                rs.getLong("time_id"),
                rs.getString("start_at")
        );
    }
}
