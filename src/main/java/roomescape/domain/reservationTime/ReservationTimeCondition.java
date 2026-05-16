package roomescape.domain.reservationTime;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.exception.InvalidRequestValueException;

public record ReservationTimeCondition(LocalDate date, long themeId) {
    private static final String INVALID_DATE_NULL = "날짜는 필수입니다.";
    private static final String INVALID_DATE_FORMAT = "유효하지 않은 날짜입니다.";
    private static final String INVALID_THEME_ID = "유효하지 않은 테마 id입니다.";

    public ReservationTimeCondition {
        validateThemeId(themeId);
    }

    public ReservationTimeCondition(String date, long themeId) {
        this(parseDate(date), themeId);
    }

    private static LocalDate parseDate(String date) {
        if(date == null || date.isBlank()) {
            throw new InvalidRequestValueException(INVALID_DATE_NULL);
        }
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidRequestValueException(INVALID_DATE_FORMAT);
        }
    }

    private static long validateThemeId(long themeId) {
        if(themeId <= 0) {
            throw new InvalidRequestValueException(INVALID_THEME_ID);
        }
        return themeId;
    }
}
