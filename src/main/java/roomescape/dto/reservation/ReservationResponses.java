package roomescape.dto.reservation;

import java.util.List;
import roomescape.domain.Reservation;

public record ReservationResponses(
        List<ReservationResponse> reservations
) {
    public static ReservationResponses from(List<Reservation> reservations) {
        return new ReservationResponses(reservations.stream()
                .map(ReservationResponse::from)
                .toList());
    }
}