package roomescape.domain.reservationtime;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import roomescape.exception.InvalidValueException;

public class ReservationStartAt {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalTime value;

    private ReservationStartAt(LocalTime value) {
        this.value = value;
    }

    public static ReservationStartAt from(String value) {
        validateValue(value);
        return new ReservationStartAt(convertLocalDate(value));
    }

    private static LocalTime convertLocalDate(String value) {
        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new InvalidValueException("시작 시간 형식은 HH:mm 이어야 합니다.");
        }
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("시작 시간은 비어있을 수 없습니다.");
        }
    }

    public boolean isBeforeNow() {
        return value.isBefore(LocalTime.now());
    }

    public String toStringTime() {
        return value.format(TIME_FORMATTER);
    }

    public LocalTime getValue() {
        return value;
    }
}
