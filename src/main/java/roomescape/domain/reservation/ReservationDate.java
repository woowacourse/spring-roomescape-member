package roomescape.domain.reservation;

import roomescape.exception.InvalidReservationException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class ReservationDate {
    private final LocalDate date;

    public ReservationDate(String date) {
        validate(date);
        this.date = LocalDate.parse(date);
    }

    private void validate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidReservationException("올바르지 않은 날짜입니다.");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationDate that = (ReservationDate) o;
        return Objects.equals(date, that.date);
    }

    public LocalDate getDate() {
        return date;
    }
}
