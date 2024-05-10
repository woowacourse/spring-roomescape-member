package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.InvalidInputException;

public record ReservationTime(Long id, LocalTime startAt) {

    public static ReservationTime of(final Long id, final String startAt) {
        try {
            return new ReservationTime(id, LocalTime.parse(startAt));
        } catch (final DateTimeException exception) {
            throw InvalidInputException.of("startAt", startAt);
        }
    }

    public boolean isBefore(final LocalTime other) {
        return this.startAt.isBefore(other);
    }

    public String getStartAtAsString() {
        return startAt.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
