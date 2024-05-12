package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalDate;
import roomescape.exception.CustomBadRequest;

public record ReservationDate(LocalDate date) {

    public static ReservationDate from(final String date) {
        try {
            return new ReservationDate(LocalDate.parse(date));
        } catch (final DateTimeException exception) {
            throw new CustomBadRequest(String.format("ReservationDate(%s)가 유효하지 않습니다.", date));
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
