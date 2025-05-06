package roomescape.dto;

import java.util.List;

public record ReservationTimesWithTotalPageResponse(int totalPages, List<ReservationTimeResponse> reservationTimes) {
}
