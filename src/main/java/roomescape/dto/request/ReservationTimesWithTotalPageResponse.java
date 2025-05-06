package roomescape.dto.request;

import java.util.List;
import roomescape.dto.response.ReservationTimeResponse;

public record ReservationTimesWithTotalPageResponse(int totalPages, List<ReservationTimeResponse> reservationTimes) {
}
