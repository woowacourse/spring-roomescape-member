package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(LocalDate date, String name, long timeId, long themeId) {
    public Reservation toReservation(ReservationTime reservationTime, Theme theme) {
        return new Reservation(this.name, this.date, reservationTime, theme);
    }
}
