package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public record ReservationRequest(
        LocalDate date,
        String name,
        long timeId,
        long themeId
) {
    public Reservation fromRequest() {
        return Reservation.saveReservationOf(name, date, timeId, themeId);
    }
}
