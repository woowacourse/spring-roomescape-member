package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationCommandException;

public record ReservationCommand(String name, String date, long timeId, long themeId) {
    private static final int MAX_NAME_LENGTH = 20;

    public ReservationCommand {
        validate(name, date, timeId, themeId);
    }

    private static void validate(String name, String date, long timeId, long themeId) {
        validateName(name);
        validateDate(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ReservationCommandException(ErrorMessage.INVALID_NAME_BLANK);
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new ReservationCommandException(ErrorMessage.INVALID_NAME_LENGTH);
        }
    }

    private static void validateDate(String date) {
        if (date == null) {
            throw new ReservationCommandException(ErrorMessage.INVALID_DATE_NULL);
        }

        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new ReservationCommandException(ErrorMessage.INVALID_DATE_FORMAT);
        }
    }

    private static void validateTimeId(long timeId) {
        if (timeId <= 0) {
            throw new ReservationCommandException(ErrorMessage.INVALID_TIME_ID_FORMAT);
        }
    }

    private static void validateThemeId(long themeId) {
        if (themeId <= 0) {
            throw new ReservationCommandException(ErrorMessage.INVALID_TIME_ID_FORMAT);
        }
    }
}