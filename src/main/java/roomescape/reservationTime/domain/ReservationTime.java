package roomescape.reservationTime.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = Objects.requireNonNull(startAt, "시간은 null 일 수 없습니다.");
    }

    public static ReservationTime createWithoutId(final LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime createWithId(final Long id, final LocalTime startAt) {
        return new ReservationTime(Objects.requireNonNull(id), startAt);
    }

    public ReservationTime assignId(final Long id) {
        return new ReservationTime(Objects.requireNonNull(id), startAt);
    }

    public boolean isSameTime(final ReservationTime time) {
        return startAt.equals(time.startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof ReservationTime that)) {
            return false;
        }

        if (getId() == null && that.getId() == null) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
