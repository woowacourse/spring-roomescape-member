package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationSaveRequest(
        String name,
        String date,
        Long timeId,
        Long themeId) {

    public Reservation toModel(final ThemeResponse themeResponse, final ReservationTimeResponse timeResponse) {
        final ReservationTime time = new ReservationTime(timeResponse.id(), timeResponse.startAt());
        final Theme theme = new Theme(themeResponse.id(), themeResponse.name(), themeResponse.description(), themeResponse.thumbnail());
        return new Reservation(name, date, time, theme);
    }
}
