package roomescape.domain.reservation.dto;

import java.util.List;
import roomescape.domain.reservation.domain.Reservation;

public record ReservationResponses(
        List<ReservationResponse> reservations
) {
    public static ReservationResponses from(List<Reservation> reservations) {
        return new ReservationResponses(reservations.stream()
                .map(ReservationResponse::from)
                .toList());
    }
}
