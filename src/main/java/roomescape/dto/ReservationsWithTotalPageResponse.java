package roomescape.dto;

import java.util.List;

public record ReservationsWithTotalPageResponse(int totalPages, List<ReservationResponse> reservations) {
}
