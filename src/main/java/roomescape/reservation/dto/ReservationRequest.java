package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

public record ReservationRequest(
        LocalDate date,
        String name,
        long timeId,
        long themeId
) {

    public Reservation toReservation(Time time, Theme theme) {
        return new Reservation(name, date, time, theme);
    }

}
