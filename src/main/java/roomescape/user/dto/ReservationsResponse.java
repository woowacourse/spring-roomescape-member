package roomescape.user.dto;

import java.util.List;

public record ReservationsResponse(
        List<ReservationResponse> reservations,
        int count
) {
}
