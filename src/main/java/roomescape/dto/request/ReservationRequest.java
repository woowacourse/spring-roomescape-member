package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        @NotBlank String name,
        @NotNull LocalDate date,
        long timeId,
        long themeId

) {
    public Reservation toReservation(ReservationTime reservationTime, Theme theme) {
        return Reservation.createWithoutId(name, date, reservationTime, theme);
    }
}
