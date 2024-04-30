package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private static final long DEFAULT_ID_VALUE = 0L;

    private final long id;
    private final LocalTime startAt;

    public ReservationTime(final LocalTime startAt) {
        this(DEFAULT_ID_VALUE, startAt);
    }

    public ReservationTime(final long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime initializeIndex(final long reservationId) {
        return new ReservationTime(reservationId, startAt);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final ReservationTime that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
