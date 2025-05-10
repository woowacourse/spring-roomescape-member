package roomescape.common.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.ReservationTime;

public class ReservationTimeMapper implements RowMapper<ReservationTime> {
    @Override
    public ReservationTime mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }
}
