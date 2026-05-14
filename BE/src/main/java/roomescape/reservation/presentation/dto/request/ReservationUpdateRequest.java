package roomescape.reservation.presentation.dto.request;

import java.time.LocalDate;
import roomescape.global.exception.ReservationErrorCode;
import roomescape.global.validation.RequestValidator;

public record ReservationUpdateRequest(
        LocalDate date,
        Long timeId,
        String name
) {
    public ReservationUpdateRequest{
        RequestValidator.requireNotNull(date, ReservationErrorCode.RESERVATION_TIME_REQUIRED);
        RequestValidator.requireNotNull(timeId, ReservationErrorCode.RESERVATION_TIME_REQUIRED);
        RequestValidator.requireNotBlank(name, ReservationErrorCode.RESERVATION_NAME_REQUIRED);
    }
}

