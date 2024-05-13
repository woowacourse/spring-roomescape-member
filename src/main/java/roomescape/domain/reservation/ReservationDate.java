package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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
            throw new IllegalArgumentException("예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("예약 날짜는 비어있을 수 없습니다.");
        }
    }

    public boolean isSame(LocalDateTime dateTime) {
        LocalDate today = LocalDate.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
        return isSame(today);
    }

    public boolean isAfter(LocalDateTime dateTime) {
        LocalDate today = LocalDate.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
        return isAfter(today);
    }

    public boolean isAfter(ReservationDate other) {
        return this.value.isAfter(other.value);
    }

    private boolean isSame(LocalDate date) {
        return value.equals(date);
    }

    private boolean isAfter(LocalDate date) {
        return value.isBefore(date);
    }

    public String toStringDate() {
        return value.toString();
    }

    public LocalDate getValue() {
        return value;
    }
}
