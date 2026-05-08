package roomescape.domain.reservationTime;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationTimeConditionException;

public record ReservationTimeCondition(String date, long themeId) {
    public ReservationTimeCondition {
        validate(date, themeId);
    }

    private static void validate(String date, long themeId) {
        validateDate(date);
        validateThemeId(themeId);
    }

    private static void validateDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new ReservationTimeConditionException(ErrorMessage.INVALID_DATE_FORMAT);
        }
    }

    private static void validateThemeId(long themeId) {
        if(themeId <= 0) {
            throw new ReservationTimeConditionException(ErrorMessage.INVALID_THEME_ID);
        }
    }
}
