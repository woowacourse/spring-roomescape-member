package roomescape.reservation.application.dto;

import java.time.LocalDate;
import roomescape.global.exception.ReservationErrorCode;
import roomescape.global.validation.ValidationUtils;

public record ReservationCreateCommand(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationCreateCommand {
        ValidationUtils.requireNotBlank(name, ReservationErrorCode.RESERVATION_NAME_REQUIRED);
        ValidationUtils.requireNotNull(date, ReservationErrorCode.RESERVATION_DATE_REQUIRED);
        ValidationUtils.requireNotNull(timeId, ReservationErrorCode.RESERVATION_TIME_REQUIRED);
        ValidationUtils.requireNotNull(themeId, ReservationErrorCode.RESERVATION_THEME_REQUIRED);
    }
}
