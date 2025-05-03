package roomescape.repository;

import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;

public final class RowMappers {

    public static final RowMapper<Reservation> RESERVATION =
        (rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var date = rs.getDate("date").toLocalDate();
            var timeSlotId = rs.getLong("time_id");
            var time = rs.getTime("start_at").toLocalTime();
            var themeId = rs.getLong("theme_id");
            var themeName = rs.getString("theme_name");
            var themeDescription = rs.getString("theme_description");
            var themeThumbnail = rs.getString("theme_thumbnail");

            var timeSlot = new TimeSlot(timeSlotId, time);
            var theme = new Theme(themeId, themeName, themeDescription, themeThumbnail);
            return new Reservation(id, name, date, timeSlot, theme);
        };

    public static final RowMapper<Theme> THEME =
        (rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var description = rs.getString("description");
            var thumbnail = rs.getString("thumbnail");
            return new Theme(id, name, description, thumbnail);
        };

    public static final RowMapper<TimeSlot> TIME_SLOT = (rs, rowNum) -> {
        var id = rs.getLong("id");
        var startAt = rs.getString("start_at");
        return new TimeSlot(id, LocalTime.parse(startAt));
    };

    private RowMappers() {
    }
}
