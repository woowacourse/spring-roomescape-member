package roomescape.dto.response;

import java.util.List;

public record ReservationsWithTotalPageResponse(int totalPages, List<ReservationResponse> reservations) {
}
