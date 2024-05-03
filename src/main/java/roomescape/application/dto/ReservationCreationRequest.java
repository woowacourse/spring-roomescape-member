package roomescape.application.dto;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public record ReservationCreationRequest(String name, LocalDate date, long timeId, long themeId) {
    public Reservation toReservation(ReservationTime time, Theme theme) {
        return new Reservation(name, date, time, theme);
    }
}
