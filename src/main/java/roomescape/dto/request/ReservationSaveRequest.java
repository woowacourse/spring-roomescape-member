package roomescape.dto.request;

import java.time.LocalDate;
import roomescape.domain.roomescape.Reservation;
import roomescape.domain.roomescape.ReservationTime;
import roomescape.domain.roomescape.Theme;

public record ReservationSaveRequest(
        String name,
        LocalDate date,
        long timeId,
        long themeId
) {
    public Reservation toEntity(final ReservationTime reservationTime, final Theme theme) {
        return Reservation.of(name, date, reservationTime, theme);
    }
}
