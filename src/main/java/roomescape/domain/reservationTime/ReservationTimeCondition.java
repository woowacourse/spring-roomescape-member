package roomescape.domain.reservationTime;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationTimeConditionException;

public record ReservationTimeCondition(LocalDate date, long themeId) {
    public ReservationTimeCondition(String date, long themeId) {
        this(validateDate(date), validateThemeId(themeId));
    }

    private static LocalDate validateDate(String date) {
        if(date == null || date.isBlank()) {
            throw new ReservationTimeConditionException(ErrorMessage.INVALID_DATE_NULL);
        }
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new ReservationTimeConditionException(ErrorMessage.INVALID_DATE_FORMAT);
        }
    }

    private static long validateThemeId(long themeId) {
        if(themeId <= 0) {
            throw new ReservationTimeConditionException(ErrorMessage.INVALID_THEME_ID);
        }
        return themeId;
    }
}
