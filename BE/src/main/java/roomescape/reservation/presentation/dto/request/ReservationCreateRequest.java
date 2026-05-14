package roomescape.reservation.presentation.dto.request;

import java.time.LocalDate;
import roomescape.global.exception.ReservationErrorCode;
import roomescape.global.validation.RequestValidator;

public record ReservationCreateRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationCreateRequest{
        RequestValidator.requireNotBlank(name, ReservationErrorCode.RESERVATION_NAME_REQUIRED);
        RequestValidator.requireNotNull(date, ReservationErrorCode.RESERVATION_TIME_REQUIRED);
        RequestValidator.requireNotNull(timeId, ReservationErrorCode.RESERVATION_TIME_REQUIRED);
        RequestValidator.requireNotNull(themeId, ReservationErrorCode.RESERVATION_THEME_REQUIRED);
    }
}
