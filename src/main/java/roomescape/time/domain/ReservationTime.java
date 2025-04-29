package roomescape.time.domain;

import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime time) {
        this.id = id;
        this.startAt = time;
    }

    public static ReservationTime create(LocalTime time) {
        return new ReservationTime(null, time);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
