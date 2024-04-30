package roomescape.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import roomescape.exception.InvalidReservationException;

public class ReservationDate {
    private final LocalDate value;

    public ReservationDate(final String value) {
        validate(value);
        this.value = LocalDate.parse(value);
    }

    private void validate(final String value) {
        try {
            LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new InvalidReservationException("올바르지 않은 날짜입니다.");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationDate that = (ReservationDate) o;
        return Objects.equals(value, that.value);
    }

    public String getValue() {
        return value.format(DateTimeFormatter.ISO_DATE);
    }
}
