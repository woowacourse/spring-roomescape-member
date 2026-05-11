package roomescape.reservation.domain;

import java.time.LocalTime;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;
    private final LocalTime endAt;

    public ReservationTime(LocalTime startAt, LocalTime endAt) {
        this(null, startAt, endAt);
    }

    public ReservationTime(Long id, LocalTime startAt, LocalTime endAt) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public ReservationTime withId(Long id) {
        return new ReservationTime(id, startAt, endAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public LocalTime getEndAt() {
        return endAt;
    }
}
