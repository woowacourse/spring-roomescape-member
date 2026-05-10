package roomescape.dto.reservationTime;

import java.util.List;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponses(
        List<ReservationTimeResponse> times
) {
    public static ReservationTimeResponses from(List<ReservationTime> reservationTimes) {
        return new ReservationTimeResponses(reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList());
    }
}