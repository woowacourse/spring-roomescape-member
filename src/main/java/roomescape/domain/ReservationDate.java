package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationDate {
    public static final String DATE_SHOULD_NOT_BE_NULL = "날짜를 입력해야 합니다.";
    private final LocalDate date;

    private ReservationDate(LocalDate date) {
        this.date = date;
    }

    public static ReservationDate from(String date) {
        validateIsNull(date);
        return new ReservationDate(LocalDate.parse(date));
    }

    private static void validateIsNull(String date) {
        if (date == null) {
            throw new IllegalArgumentException(DATE_SHOULD_NOT_BE_NULL);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationDate that = (ReservationDate) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
