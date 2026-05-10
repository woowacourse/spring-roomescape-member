package roomescape.presentation.dto;

import java.time.LocalTime;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.InvalidRequestException;

public record ReservationTimeRequest(
        LocalTime startAt
) {
    public ReservationTimeRequest {
        validateStartAtNotEmpty(startAt);
    }

    private void validateStartAtNotEmpty(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidRequestException(ErrorCode.RESERVATION_TIME_NULL);
        }
    }
}
