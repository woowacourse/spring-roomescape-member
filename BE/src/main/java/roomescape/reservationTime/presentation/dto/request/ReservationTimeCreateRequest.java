package roomescape.reservationTime.presentation.dto.request;

import java.time.LocalTime;
import roomescape.global.exception.ReservationTimeErrorCode;
import roomescape.global.validation.RequestValidator;

public record ReservationTimeCreateRequest(
        LocalTime startAt
) {
    public ReservationTimeCreateRequest {
        RequestValidator.requireNotNull(startAt, ReservationTimeErrorCode.RESERVATION_TIME_START_AT_REQUIRED);
    }
}
