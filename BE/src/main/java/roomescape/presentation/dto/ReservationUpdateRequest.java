package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.InvalidRequestException;

public record ReservationUpdateRequest(
        LocalDate date,
        Long timeId
) {
    public ReservationUpdateRequest {
        validateDateNotEmpty(date);
        validateTimeIdNotEmpty(timeId);
    }

    private static void validateTimeIdNotEmpty(Long timeId) {
        if (timeId == null) {
            throw new InvalidRequestException(ErrorCode.RESERVATION_UPDATE_TIME_ID_EMPTY);
        }
    }

    private static void validateDateNotEmpty(LocalDate date) {
        if (date == null) {
            throw new InvalidRequestException(ErrorCode.RESERVATION_UPDATE_DATE_EMPTY);
        }
    }
}
