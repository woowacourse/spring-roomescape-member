package roomescape.time.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.time.domain.ReservationTime;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TimeRowMapper implements RowMapper<ReservationTime> {

    @Override
    public ReservationTime mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getString("start_at")
        );
    }
}
