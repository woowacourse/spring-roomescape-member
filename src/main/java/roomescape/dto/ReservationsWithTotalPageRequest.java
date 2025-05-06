package roomescape.dto;

import java.util.List;

public record ReservationsWithTotalPageRequest(int totalPages, List<ReservationResponse> reservations) {
}
