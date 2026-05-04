package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private Long id;
    private final LocalTime startAt;

    public static ReservationTime createWithoutId(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        ReservationTime reservationTime = (ReservationTime) object;
        if (id != null && reservationTime.id != null) {
            return Objects.equals(id, reservationTime.id);
        }
        return Objects.equals(startAt, reservationTime.startAt);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(startAt);
    }
}
