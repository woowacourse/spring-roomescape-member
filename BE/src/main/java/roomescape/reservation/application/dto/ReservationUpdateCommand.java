package roomescape.reservation.application.dto;

import java.time.LocalDate;
import roomescape.global.exception.ReservationErrorCode;
import roomescape.global.validation.ValidationUtils;


public record ReservationUpdateCommand(
    Long id,
    LocalDate date,
    Long timeId,
    String name
) {
    public ReservationUpdateCommand {
        ValidationUtils.requireNotNull(id, ReservationErrorCode.RESERVATION_ID_REQUIRED);
        ValidationUtils.requireNotNull(date, ReservationErrorCode.RESERVATION_DATE_REQUIRED);
        ValidationUtils.requireNotNull(timeId, ReservationErrorCode.RESERVATION_TIME_REQUIRED);
        ValidationUtils.requireNotBlank(name, ReservationErrorCode.RESERVATION_NAME_REQUIRED);
    }
}
