package roomescape.presentation.response;

import java.time.LocalTime;
import roomescape.application.result.AvailableReservationTimeResult;

public record AvailableReservationTimeResponse(Long timeId, LocalTime startAt, boolean booked) {
    public static AvailableReservationTimeResponse from(AvailableReservationTimeResult availableReservationTimeResult) {
        return new AvailableReservationTimeResponse(
                availableReservationTimeResult.timeId(),
                availableReservationTimeResult.startAt(),
                availableReservationTimeResult.booked()
        );
    }
}
