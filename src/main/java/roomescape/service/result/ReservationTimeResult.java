package roomescape.service.result;

import java.time.LocalTime;

public record ReservationTimeResult(
        Long id,
        LocalTime startAt
) {
    public static ReservationTimeResult from(ReservationTimeResult reservationTimeResult) {
        return new ReservationTimeResult(reservationTimeResult.id(), reservationTimeResult.startAt());
    }
}
