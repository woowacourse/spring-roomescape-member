package roomescape.domain;

import java.time.LocalTime;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime of(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }

    public static ReservationTime withoutId(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime assignId(Long id, ReservationTime reservationTime) {
        return new ReservationTime(id, reservationTime.startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
