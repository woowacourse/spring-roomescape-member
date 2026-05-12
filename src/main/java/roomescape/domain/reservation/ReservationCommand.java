package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationCommandException;

public record ReservationCommand(String name, LocalDate date, long timeId, long themeId) {
    private static final int MAX_NAME_LENGTH = 20;

    public ReservationCommand {
        validate(name, date, timeId, themeId);
    }

    private static void validate(String name, LocalDate date, long timeId, long themeId) {
        validateName(name);
        validateDate(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
    }

    private static void validateName(String name) {
        // 이건 DTO에서 검증
//        if (name == null || name.isBlank()) {
//            throw new ReservationCommandException(ErrorMessage.INVALID_NAME_BLANK);
//        }

        /**
         * 이 또한 DTO로 이동
         */
//        if (name.length() > MAX_NAME_LENGTH) {
//            throw new ReservationCommandException(ErrorMessage.INVALID_NAME_LENGTH);
//        }
    }

    private static void validateDate(LocalDate date) {
        // @Valid로 필드 에러 처리 가능
//        if (date == null) {
//            throw new ReservationCommandException(ErrorMessage.INVALID_DATE_NULL);
//        }

        // 이미 생성자로 들어올 때 LocalDate로 들어옴
//        try {
//            LocalDate.parse(date);
//        } catch (DateTimeParseException e) {
//            throw new ReservationCommandException(ErrorMessage.INVALID_DATE_FORMAT);
//        }
        /**
         * 지난 날짜인지 체크 필요
         */
    }

    private static void validateTimeId(long timeId) {
        /**
         * 음수값은 DTO에서 검증해도 될 듯?
         */
//        if (timeId <= 0) {
//            throw new ReservationCommandException(ErrorMessage.INVALID_TIME_ID_FORMAT);
//        }
    }

    private static void validateThemeId(long themeId) {
        /**
         * 음수값은 DTO에서 검증해도 될 듯?
         */
//        if (themeId <= 0) {
//            throw new ReservationCommandException(ErrorMessage.INVALID_TIME_ID_FORMAT);
//        }
    }
}