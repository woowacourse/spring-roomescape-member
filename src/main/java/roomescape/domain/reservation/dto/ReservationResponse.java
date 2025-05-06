package roomescape.domain.reservation.dto;

import java.time.LocalDate;
import roomescape.domain.reservation.entity.Reservation;

public record ReservationResponse(Long id, String name, LocalDate date,
                                  ReservationTimeResponse time, ThemeResponse theme) {

    public static ReservationResponse from(final Reservation reservation) {
        final ReservationTimeResponse reservationTimeResponse = ReservationTimeResponse.from(
                reservation.getReservationTime());

        final ThemeResponse themeResponse = ThemeResponse.from(reservation.getTheme());

        return new ReservationResponse(reservation.getId(),
                reservation.getName(),
                reservation.getReservationDate(),
                reservationTimeResponse,
                themeResponse
        );
    }
}
