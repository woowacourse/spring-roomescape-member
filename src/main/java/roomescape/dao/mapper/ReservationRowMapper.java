package roomescape.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Component
public class ReservationRowMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        long reservationId = rs.getLong("reservation_id");
        String name = rs.getString("name");
        String date = rs.getString("date");
        long timeId = rs.getLong("time_id");
        String startAt = rs.getString("time_value");
        long themeId = rs.getLong("theme_id");
        String themeName = rs.getString("theme_name");
        String description = rs.getString("theme_description");
        String thumbnail = rs.getString("theme_thumbnail");
        return new Reservation(
                reservationId,
                new Name(name),
                ReservationDate.from(date),
                ReservationTime.from(timeId, startAt),
                Theme.of(themeId, themeName, description, thumbnail)
        );
    }
}
