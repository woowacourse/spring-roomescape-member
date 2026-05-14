package roomescape.reservationTime.presentation.dto.request;

import java.time.LocalTime;
import roomescape.global.exception.ReservationTimeErrorCode;
import roomescape.global.exception.customException.BadRequestException;

public record ReservationTimeCreateRequest(
        LocalTime startAt
) {
    public ReservationTimeCreateRequest {
        validateStartAtNotEmpty(startAt);
    }

    private void validateStartAtNotEmpty(LocalTime startAt) {
        if (startAt == null) {
            throw new BadRequestException(ReservationTimeErrorCode.RESERVATION_TIME_START_AT_REQUIRED);
        }
    }
}
