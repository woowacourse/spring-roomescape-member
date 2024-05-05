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
    public Reservation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final long reservationId = rs.getLong("reservation_id");
        final String name = rs.getString("name");
        final String date = rs.getString("date");
        final long timeId = rs.getLong("time_id");
        final String startAt = rs.getString("time_value");
        final long themeId = rs.getLong("theme_id");
        final String themeName = rs.getString("theme_name");
        final String description = rs.getString("theme_description");
        final String thumbnail = rs.getString("theme_thumbnail");
        return new Reservation(
                reservationId,
                new Name(name),
                ReservationDate.from(date),
                ReservationTime.from(timeId, startAt),
                Theme.of(themeId, themeName, description, thumbnail)
        );
    }
}
