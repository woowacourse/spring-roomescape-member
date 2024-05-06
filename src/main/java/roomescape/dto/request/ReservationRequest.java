package roomescape.dto.request;

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
    public Reservation toEntity(final ReservationTime reservationTime, final Theme theme) {
        return Reservation.of(name, date, reservationTime, theme);
    }
}
