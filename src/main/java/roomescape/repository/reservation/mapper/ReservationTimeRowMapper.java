package roomescape.repository.reservation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.reservation.ReservationTime;

public class ReservationTimeRowMapper implements RowMapper<ReservationTime> {

    public static final ReservationTimeRowMapper INSTANCE = new ReservationTimeRowMapper();

    private ReservationTimeRowMapper() {
    }

    @Override
    public ReservationTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReservationTime(
                rs.getLong("id"),
                rs.getTime("start_at").toLocalTime()
        );
    }
}
