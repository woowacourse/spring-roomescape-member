package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.InvalidRequestException;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationRequest {
        validateNameNotEmpty(name);
        validateDateNotEmpty(date);
        validateTimeIdNotEmpty(timeId);
    }

    private static void validateNameNotEmpty(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException(ErrorCode.RESERVATION_NAME_EMPTY);
        }
    }

    private static void validateTimeIdNotEmpty(Long timeId) {
        if (timeId == null) {
            throw new InvalidRequestException(ErrorCode.RESERVATION_TIME_NULL);
        }
    }

    private static void validateDateNotEmpty(LocalDate date) {
        if (date == null) {
            throw new InvalidRequestException(ErrorCode.RESERVATION_DATE_NULL);
        }
    }
}
