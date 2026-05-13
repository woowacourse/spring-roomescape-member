package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.exception.InvalidRequestValueException;

public record ReservationCommand(String name, LocalDate date, long timeId, long themeId) {
    private static final int MAX_NAME_LENGTH = 20;

    private static final String INVALID_DATE_NULL = "날짜는 필수입니다.";
    private static final String INVALID_DATE_FORMAT = "유효하지 않은 날짜 형식입니다.";
    private static final String NOT_RESERVABLE_PAST_DATE = "예약일은 현재 날짜보다 이전일 수 없습니다.";
    private static final String INVALID_TIME_ID_FORMAT = "시간 id는 0보다 커야 합니다.";
    private static final String INVALID_THEME_ID_FORMAT = "테마 id 0보다 커야 합니다.";
    private static final String INVALID_NAME_BLANK = "이름은 필수입니다.";
    private static final String INVALID_NAME_LENGTH = "이름은 20자를 초과할 수 없습니다.";


    public ReservationCommand {
        validateName(name);
        validateDateValue(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
    }

    public ReservationCommand(String name, String date, long timeId, long themeId) {
        this(name, parseDate(date), timeId, themeId);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestValueException(INVALID_NAME_BLANK);
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidRequestValueException(INVALID_NAME_LENGTH);
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
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidRequestValueException(NOT_RESERVABLE_PAST_DATE);
        }
    }

    private static void validateTimeId(long timeId) {
        if (timeId <= 0) {
            throw new InvalidRequestValueException(INVALID_TIME_ID_FORMAT);
        }
    }

    private static void validateThemeId(long themeId) {
        if (themeId <= 0) {
            throw new InvalidRequestValueException(INVALID_THEME_ID_FORMAT);
        }
    }
}
