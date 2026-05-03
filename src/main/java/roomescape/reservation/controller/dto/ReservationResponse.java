package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.time.controller.dto.ReservationTimeResponse;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().toString(),
                ReservationTimeResponse.from(reservation.getTime())
        );
    }
}
