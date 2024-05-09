package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalDate;
import roomescape.exception.InvalidInputException;

public record ReservationDate(LocalDate date) {

    public static ReservationDate from(final String date) {
        try {
            return new ReservationDate(LocalDate.parse(date));
        } catch (final DateTimeException exception) {
            throw InvalidInputException.of("ReservationDate", date);
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
