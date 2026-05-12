package roomescape.controller.dto.reservation;

import java.util.List;

public record ReservationResponses(
        List<ReservationResponse> reservations
) {
    public ReservationResponses {
        reservations = List.copyOf(reservations);
    }
}
