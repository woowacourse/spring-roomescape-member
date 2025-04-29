package roomescape.reservationtime.domain;

import java.time.LocalTime;
import java.util.Objects;
import roomescape.common.domain.Id;

public class ReservationTime {
    private final Id id;
    private final LocalTime startAt;

    private ReservationTime(final Id id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    private ReservationTime(final Id id, final String startAt) {
        this.id = id;
        this.startAt = LocalTime.parse(startAt);
    }

    public static ReservationTime of(final Long id, final LocalTime startAt) {
        return new ReservationTime(Id.from(id), startAt);
    }

    public static ReservationTime of(final Long id, final String startAt) {
        return new ReservationTime(Id.from(id), startAt);
    }

    public static ReservationTime withUnassignedId(final LocalTime startAt) {
        return new ReservationTime(Id.unassigned(), startAt);
    }

    public Long getId() {
        return id.getValue();
    }

    public void setId(Long value) {
        id.setValue(value);
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getStartAt(), that.getStartAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStartAt());
    }
}
