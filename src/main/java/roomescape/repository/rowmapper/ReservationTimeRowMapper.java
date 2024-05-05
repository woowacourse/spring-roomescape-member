package roomescape.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.ReservationTime;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReservationTimeRowMapper implements RowMapper<ReservationTime> {
    @Override
    public ReservationTime mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return ReservationTime.builder()
                    .timeId(resultSet.getLong("id"))
                    .startAt(resultSet.getString("start_at"))
                    .build();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
