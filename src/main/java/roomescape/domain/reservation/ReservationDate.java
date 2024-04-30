package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReservationDate {

    private final LocalDate value;

    private ReservationDate(LocalDate value) {
        this.value = value;
    }

    public static ReservationDate from(String value) {
        validateValue(value);
        return new ReservationDate(convertLocalDate(value));
    }

    private static LocalDate convertLocalDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }
    }

    private static void validateValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("예약 날짜는 비어있을 수 없습니다.");
        }
    }

    public String toStringDate() {
        return value.toString();
    }
}
