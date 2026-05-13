package roomescape.domain.reservation.dto;

import java.util.List;
import roomescape.domain.reservation.Reservation;

public record ReservationsResponse(
    List<ReservationResponse> reservations
) {

    public static ReservationsResponse from(List<Reservation> reservations) {
        return new ReservationsResponse(reservations.stream()
            .map(ReservationResponse::from)
            .toList()
        );
    }
}
