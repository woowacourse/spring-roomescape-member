package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final ReservationTimeId id;
    private final ReservationTimeStartAt startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        this(id == null ? null : new ReservationTimeId(id), new ReservationTimeStartAt(startAt));
    }

    private ReservationTime(ReservationTimeId id, ReservationTimeStartAt startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime withId(Long id) {
        ReservationTimeId reservationTimeId = new ReservationTimeId(id);

        if (this.id != null) {
            throw new DomainException(ErrorCode.RESERVATION_TIME_ALREADY_HAS_ID);
        }

        return new ReservationTime(reservationTimeId, startAt);
    }

    public Long getId() {
        if (id == null) {
            return null;
        }
        return id.value();
    }

    public LocalTime getStartAt() {
        return startAt.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationTime that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
