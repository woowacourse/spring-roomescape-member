package roomescape.reservation.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.ReservationTime;

@Component
public class ReservationTimeRowMapper implements RowMapper<ReservationTime> {
    @Override
    public ReservationTime mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return ReservationTime.of(
                    resultSet.getLong("time_id"),
                    resultSet.getString("start_at")
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
