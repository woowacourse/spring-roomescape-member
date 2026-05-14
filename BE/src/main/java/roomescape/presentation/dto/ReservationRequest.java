package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.global.exception.ReservationErrorCode;
import roomescape.global.exception.customException.BadRequestException;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationRequest{
        validateNameNotEmpty(name);
        validateDateNotEmpty(date);
        validateTimeIdNotEmpty(timeId);
        validateThemeIdNotEmpty(themeId);
    }

    private static void validateNameNotEmpty(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_NAME_REQUIRED);
        }
    }
    
    private static void validateTimeIdNotEmpty(Long timeId) {
        if (timeId == null) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_TIME_REQUIRED);
        }
    }

    private static void validateThemeIdNotEmpty(Long themeId) {
        if (themeId == null) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_THEME_REQUIRED);
        }
    }

    private static void validateDateNotEmpty(LocalDate date) {
        if (date == null) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_DATE_REQUIRED);
        }
    }
}
