package roomescape.time.repository;

import org.springframework.jdbc.core.RowMapper;
import roomescape.time.domain.ReservationTime;

public class ReservationTimeRowMapper {
    private ReservationTimeRowMapper() {
    }

    public static final RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER = (resultSet, rowNum) ->
            ReservationTime.of(
                    resultSet.getLong("id"),
                    resultSet.getTime("start_at").toLocalTime());
}
