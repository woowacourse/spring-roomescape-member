package roomescape.presentation.dto;

import roomescape.entity.Reservation;

public record AvailableReservationResponse(
        String name,
        String date,
        ReservationTimeResponse time,
        ThemeResponse theme,
        boolean isAvailable
) {
    public static AvailableReservationResponse from(Reservation reservation) {
        return new AvailableReservationResponse(
                reservation.name(),
                reservation.date().toString(),
                ReservationTimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.theme()),
                reservation.id() == null
        );
    }
}
