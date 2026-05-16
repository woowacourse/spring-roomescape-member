package roomescape.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationStatus;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public final class ReservationEntityMapper {

    public static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) -> {
        ReservationTime time = ReservationTime.restore(
                rs.getLong("time_id"),
                rs.getTime("time_start").toLocalTime(),
                rs.getBoolean("time_active")
        );
        Theme theme = Theme.restore(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("description"),
                rs.getString("thumbnail_image_url"),
                rs.getBoolean("theme_active")
        );
        return Reservation.restore(
                rs.getLong("res_id"),
                rs.getString("res_name"),
                rs.getDate("res_date").toLocalDate(),
                theme,
                time,
                ReservationStatus.valueOf(rs.getString("res_status"))
        );
    };

    private ReservationEntityMapper() {
    }
}
