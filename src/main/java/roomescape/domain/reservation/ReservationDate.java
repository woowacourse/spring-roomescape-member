package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.exception.InvalidValueException;

public class ReservationDate {

    private final LocalDate value;

    private ReservationDate(LocalDate value) {
        this.value = value;
    }

    public static ReservationDate from(String value) {
        validateValue(value);
        LocalDate reservationDate = convertLocalDate(value);
        return new ReservationDate(reservationDate);
    }

    private static LocalDate convertLocalDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new InvalidValueException("예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("예약 날짜는 비어있을 수 없습니다.");
        }
    }

    public boolean isSameDate(LocalDate date) {
        return value.equals(date);
    }

    public boolean isBeforeDate(LocalDate date) {
        return value.isBefore(date);
    }

    public String toStringDate() {
        return value.toString();
    }

    public LocalDate getValue() {
        return value;
    }
}
