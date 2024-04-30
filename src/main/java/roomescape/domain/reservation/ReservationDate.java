package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReservationDate {

    private static final LocalDate TODAY = LocalDate.now();

    private final LocalDate value;

    private ReservationDate(LocalDate value) {
        this.value = value;
    }

    public static ReservationDate from(String value) {
        validateValue(value);
        LocalDate reservationDate = convertLocalDate(value);
        validateDate(reservationDate);
        return new ReservationDate(reservationDate);
    }

    private static void validateDate(LocalDate reservationDate) {
        if (TODAY.isAfter(reservationDate)) {
            throw new IllegalArgumentException("예약일은 오늘보다 과거일 수 없습니다.");
        }
    }

    private static LocalDate convertLocalDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("예약 날짜는 비어있을 수 없습니다.");
        }
    }

    public boolean isToday() {
        return TODAY.equals(value);
    }

    public String toStringDate() {
        return value.toString();
    }
}
