package roomescape.domain.reservationtime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("시작 시간은 비어있을 수 없습니다.");
        }
    }

    private static LocalTime convertLocalDate(String value) {
        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("시작 시간 형식은 HH:mm 이어야 합니다.");
        }
    }

    public boolean isBefore(LocalDateTime dateTime) {
        LocalTime time = LocalTime.of(dateTime.getHour(), dateTime.getMinute());
        return isBefore(time);
    }

    private boolean isBefore(LocalTime time) {
        return value.isBefore(time);
    }

    public String toStringTime() {
        return value.format(TIME_FORMATTER);
    }

    public LocalTime getValue() {
        return value;
    }
}
