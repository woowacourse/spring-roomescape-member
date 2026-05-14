package roomescape.controller.dto.reservation;

import java.util.List;

public record ReservationsResponse(List<ReservationResponse> reservations) {
}
