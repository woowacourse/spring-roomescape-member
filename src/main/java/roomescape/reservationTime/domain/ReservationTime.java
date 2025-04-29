package roomescape.reservationTime.domain;

import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime time) {
        this.id = null;
        this.startAt = time;
    }

    public boolean isSameTime(ReservationTime reservationTime) {
        return this.startAt.equals(reservationTime.startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
