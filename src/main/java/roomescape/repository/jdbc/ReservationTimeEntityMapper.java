package roomescape.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.ReservationTime;

public final class ReservationTimeEntityMapper {

    public static final RowMapper<ReservationTime> RESERVATION_TIME_MAPPER = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    );

    private ReservationTimeEntityMapper() {}
}
