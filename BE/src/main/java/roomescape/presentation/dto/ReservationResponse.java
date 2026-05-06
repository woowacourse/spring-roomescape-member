package roomescape.presentation.dto;

import roomescape.entity.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date().toString(),
                ReservationTimeResponse.from(reservation.time())
        );
    }
}
