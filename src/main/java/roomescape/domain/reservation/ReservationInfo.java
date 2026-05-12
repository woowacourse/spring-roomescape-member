package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.InvalidRequestValueException;

public record ReservationInfo(long id, String name, LocalDate date, ReservationTime time, Theme reservationTheme) {
    private static final String INVALID_DATE_NULL = "날짜는 필수입니다.";
    private static final String INVALID_DATE_FORMAT = "유효하지 않은 날짜입니다.";

    public ReservationInfo(long id, String name, String date, ReservationTime time, Theme theme) {
        this(id, name, validateDate(date), time, theme);
    }

    public static LocalDate validateDate(String date) {
        if(date == null || date.isBlank()) {
            throw new InvalidRequestValueException(INVALID_DATE_NULL);
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidRequestValueException(INVALID_DATE_FORMAT);
        }
    }
}
