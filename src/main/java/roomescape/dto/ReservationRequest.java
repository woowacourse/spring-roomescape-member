package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        String name,
        LocalDate date,
        long timeId,
        long themeId

) {
    public Reservation toReservation(ReservationTime reservationTime, Theme theme) {
        return Reservation.createWithoutId(name, date, reservationTime, theme);
    }
}
