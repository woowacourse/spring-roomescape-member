package roomescape.reservationTime.application.dto;

import java.time.LocalTime;
import roomescape.global.exception.ReservationTimeErrorCode;
import roomescape.global.validation.ValidationUtils;

public record ReservationTimeCreateCommand(
        LocalTime startAt
) {
    public ReservationTimeCreateCommand {
        ValidationUtils.requireNotNull(startAt, ReservationTimeErrorCode.RESERVATION_TIME_START_AT_REQUIRED);
    }
}
