package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.InvalidRequestValueException;

public record ReservationInfo(long id, String name, LocalDate date, ReservationTime time, Theme reservationTheme) {
    private static final String INVALID_NAME_NULL = "이름은 필수입니다.";
    private static final String INVALID_DATE_NULL = "날짜는 필수입니다.";
    private static final String INVALID_DATE_FORMAT = "유효하지 않은 날짜입니다.";
    private static final String INVALID_TIME_NULL = "시간 정보는 필수입니다.";
    private static final String INVALID_THEME_NULL = "테마 정보는 필수입니다.";

    public ReservationInfo {
        validateName(name);
        validateDateValue(date);
        validateObjects(time, reservationTheme);
    }

    public ReservationInfo(long id, String name, String date, ReservationTime time, Theme theme) {
        this(id, name, parseDate(date), time, theme);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestValueException(INVALID_NAME_NULL);
        }
    }

    private static LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            throw new InvalidRequestValueException(INVALID_DATE_NULL);
        }
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidRequestValueException(INVALID_DATE_FORMAT);
        }
    }

    private static void validateDateValue(LocalDate date) {
        if (date == null) {
            throw new InvalidRequestValueException(INVALID_DATE_NULL);
        }
    }

    private static void validateObjects(ReservationTime time, Theme theme) {
        if (time == null) {
            throw new InvalidRequestValueException(INVALID_TIME_NULL);
        }
        if (theme == null) {
            throw new InvalidRequestValueException(INVALID_THEME_NULL);
        }
    }
}