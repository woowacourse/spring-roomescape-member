package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.DomainViolationException;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateTime(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    private void validateTime(LocalTime startAt) {
        if (startAt.getMinute() != 0 && startAt.getMinute() != 30) {
            throw new DomainViolationException("시간은 정각 또는 30분 단위여야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public ReservationTime withId(long id) {
        return new ReservationTime(id, this.startAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return id != null
            && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
