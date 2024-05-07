package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalDate;

public record ReservationDate(LocalDate date) {

    public static ReservationDate from(final String date) {
        try {
            return new ReservationDate(LocalDate.parse(date));
        } catch (final DateTimeException exception) {
            throw new IllegalArgumentException(String.format("%s 는 유효하지 않은 값입니다.(EX: 10:00)", date));
        }
    }

    public boolean isBefore(final LocalDate other) {
        return this.date.isBefore(other);
    }

    public String asString() {
        return date.toString();
    }

    public boolean isEqual(final LocalDate date) {
        return this.date.equals(date);
    }
}
