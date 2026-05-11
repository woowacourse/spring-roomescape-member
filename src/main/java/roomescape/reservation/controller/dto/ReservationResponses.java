package roomescape.reservation.controller.dto;

import java.util.List;

public record ReservationResponses(
        List<ReservationResponse> reservations
) {
}
