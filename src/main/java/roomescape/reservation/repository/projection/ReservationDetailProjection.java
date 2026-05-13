package roomescape.reservation.repository.projection;

import roomescape.reservationtime.dto.response.TimeInformation;
import roomescape.theme.dto.response.ThemeFindResponse;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDetailProjection(
        Long id,
        String name,
        LocalDate date,
        ThemeFindResponse themeFindResponse,
        TimeInformation timeInformation
) {
    public long getTimeId() {
        return timeInformation().id();
    }

    public long getThemeId() {
        return themeFindResponse().id();
    }

    public LocalTime getTime() {
        return timeInformation().time();
    }
}
