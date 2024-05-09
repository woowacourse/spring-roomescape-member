package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationDate {

    private final LocalDate value;

    public ReservationDate(final LocalDate value) {
        validateDate(value);
        this.value = value;
    }

    private void validateDate(final LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("예약 날짜는 공백을 입력할 수 없습니다.");
        }
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final ReservationDate that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
