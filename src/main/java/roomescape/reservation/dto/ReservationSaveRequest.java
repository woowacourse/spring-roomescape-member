package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.ReservationName;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationSaveRequest(
        @NotNull @NotBlank String name,
        @NotNull LocalDate date,
        @NotNull Long themeId,
        @NotNull Long timeId) {

    public Reservation toReservation(Theme theme, ReservationTime reservationTime) {
        return new Reservation(new ReservationName(name), date, theme, reservationTime);
    }
}
