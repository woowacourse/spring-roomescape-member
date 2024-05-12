package roomescape.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.AvailableReservationTime;


@Component
public class AvailableReservationTimeRowMapper implements RowMapper<AvailableReservationTime> {

    @Override
    public AvailableReservationTime mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new AvailableReservationTime(
                rs.getString("start_at"),
                rs.getLong("time_id"),
                rs.getBoolean("already_booked")
        );
    }
}
