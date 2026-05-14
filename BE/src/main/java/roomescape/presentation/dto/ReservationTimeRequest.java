package roomescape.presentation.dto;

import java.time.LocalTime;
import roomescape.global.exception.ReservationTimeErrorCode;
import roomescape.global.exception.customException.BadRequestException;

public record ReservationTimeRequest(
        LocalTime startAt
) {
    public ReservationTimeRequest {
        validateStartAtNotEmpty(startAt);
    }

    private void validateStartAtNotEmpty(LocalTime startAt) {
        if (startAt == null) {
            throw new BadRequestException(ReservationTimeErrorCode.RESERVATION_TIME_START_AT_REQUIRED);
        }
    }
}
