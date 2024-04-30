package roomescape.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;

@Component
public class ReservationRowMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                resultSet.getLong("time_id"),
                resultSet.getString("time_value")
        );
    }
}
