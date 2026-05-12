package roomescape.time.dto;

import java.util.List;

public record ReservationTimesResponse(
        List<ReservationTimeResponse> reservationTimes
) {
    public static ReservationTimesResponse from(List<ReservationTimeResponse> reservationTimes) {
        return new ReservationTimesResponse(reservationTimes);
    }
}
