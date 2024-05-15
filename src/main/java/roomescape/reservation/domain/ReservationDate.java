package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import roomescape.exception.RoomEscapeException;

public class ReservationDate {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final LocalDate date;

    public ReservationDate(final String date) {
        this.date = parseDate(date);
    }

    private LocalDate parseDate(final String date) {
        try {
            return LocalDate.parse(date, DATE_FORMAT);
        } catch (NullPointerException | DateTimeParseException e) {
            throw new RoomEscapeException("형식에 맞지 않은 날짜입니다.");
        }
    }

    public boolean isBeforeToday() {
        return date.isBefore(LocalDate.now());
    }

    public boolean isToday() {
        return date.equals(LocalDate.now());
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationDate that = (ReservationDate) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }
}
