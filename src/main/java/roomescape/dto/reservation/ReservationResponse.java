package roomescape.dto.reservation;

import roomescape.domain.reservation.Reservation;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

import java.time.LocalDate;

public record ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time, ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                ReservationTimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.theme())
        );
    }
}
