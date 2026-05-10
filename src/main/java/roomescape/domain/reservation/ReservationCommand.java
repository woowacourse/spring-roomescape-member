package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationCommandException;

public record ReservationCommand(String name, LocalDate date, long timeId, long themeId) {
    private static final int MAX_NAME_LENGTH = 20;

    public ReservationCommand(String name, String date, long timeId, long themeId) {
        this(validateName(name), validateDate(date), validateTimeId(timeId), validateThemeId(themeId));
    }

    private static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ReservationCommandException(ErrorMessage.INVALID_NAME_BLANK);
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new ReservationCommandException(ErrorMessage.INVALID_NAME_LENGTH);
        }

        return name;
    }

    private static LocalDate validateDate(String date) {
        if (date == null || date.isBlank()) {
            throw new ReservationCommandException(ErrorMessage.INVALID_DATE_NULL);
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new ReservationCommandException(ErrorMessage.INVALID_DATE_FORMAT);
        }
    }

    private static long validateTimeId(long timeId) {
        if (timeId <= 0) {
            throw new ReservationCommandException(ErrorMessage.INVALID_TIME_ID_FORMAT);
        }

        return timeId;
    }

    private static long validateThemeId(long themeId) {
        if (themeId <= 0) {
            throw new ReservationCommandException(ErrorMessage.INVALID_THEME_ID_FORMAT);
        }

        return themeId;
    }
}
