package roomescape.reservationTime.dto;

import java.util.List;

public record ReservationTimesResponse(
        List<ReservationTimeResponse> reservationTimes,
        int count
) {
}
