package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.ArgumentNullException;

public class ReservationTime {

    private Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime createWithoutId(final LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    private void validateNull(LocalTime startAt) {
        if (startAt == null) {
            throw new ArgumentNullException("startAt");
        }
    }

    public ReservationTime withId(Long id) {
        return new ReservationTime(id, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id) && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
