package roomescape.infrastructure.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.ReservationTime;

public class ReservationTimeRowMapper implements RowMapper<ReservationTime> {

    private static final ReservationTimeRowMapper INSTANCE = new ReservationTimeRowMapper();

    private ReservationTimeRowMapper() {
    }

    public static ReservationTimeRowMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ReservationTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String startAt = rs.getString("start_at");
        return new ReservationTime(id, LocalTime.parse(startAt));
    }
}
