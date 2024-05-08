package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.isNull;

public class ReservationDate {
    private final LocalDate startDate;

    public ReservationDate(LocalDate startDate) {
        if (isNull(startDate)) {
            throw new IllegalArgumentException("start date must not be null");
        }
        this.startDate = startDate;
    }

    public LocalDate getStartAt() {
        return startDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ReservationDate that = (ReservationDate) o;
        return Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(startDate);
    }
}
