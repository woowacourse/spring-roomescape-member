package roomescape.controller.dto.reservationtime;

import java.util.List;

public record ReservationTimeResponses(
        List<ReservationTimeResponse> reservationTimes
) {
    public ReservationTimeResponses {
        reservationTimes = List.copyOf(reservationTimes);
    }
}
