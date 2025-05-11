package roomescape.reservationtime.dto.response;

import java.util.List;

public record ReservationTimesWithTotalPageResponse(int totalPages, List<ReservationTimeResponse> reservationTimes) {
}
