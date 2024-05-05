package roomescape.time.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import roomescape.time.domain.ReservationUserTime;

@Component
public class UserTimeRowMapper implements RowMapper<ReservationUserTime> {

    @Override
    public ReservationUserTime mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ReservationUserTime(
                resultSet.getLong("id"),
                resultSet.getString("start_at"),
                resultSet.getBoolean("already_booked"));
    }
}
