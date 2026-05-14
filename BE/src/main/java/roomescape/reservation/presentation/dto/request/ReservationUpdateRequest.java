package roomescape.reservation.presentation.dto.request;

import java.time.LocalDate;
import roomescape.global.exception.ReservationErrorCode;
import roomescape.global.exception.customException.BadRequestException;

public record ReservationUpdateRequest(
        LocalDate date,
        Long timeId,
        String name
) {
    public ReservationUpdateRequest{
        validateDateNotEmpty(date);
        validateTimeIdNotEmpty(timeId);
        validateNameNotEmpty(name);
    }

    private static void validateTimeIdNotEmpty(Long timeId) {
        if (timeId == null) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_TIME_REQUIRED);
        }
    }

    private static void validateDateNotEmpty(LocalDate date) {
        if (date == null) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_DATE_REQUIRED);
        }
    }

    private static void validateNameNotEmpty(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_NAME_REQUIRED);
        }
    }
}

