package roomescape.reservation.dto;

import java.time.LocalDate;

import roomescape.reservation.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        ReservationTimeResponse reservationTimeResponse = ReservationTimeResponse.from(
                reservation.getReservationTime()
        );
        ThemeResponse themeResponse = ThemeResponse.from(reservation.getTheme());

        return new ReservationResponse(reservation.getId(),
                reservation.getName().getValue(),
                reservation.getReservationDate(),
                reservationTimeResponse,
                themeResponse
        );
    }
}
