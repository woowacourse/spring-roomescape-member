package roomescape.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;

public record ReservationCreateRequest(
        @NotBlank String name,
        @NotNull LocalDate date,
        @NotNull Long timeId
) {
    public Reservation toReservation(ReservationTime reservationTime) {
        return Reservation.createWithoutId(name, date, reservationTime);
    }
}
