package roomescape.time.domain;

import java.time.LocalTime;

public class ReservationTime {

    public ReservationTime(LocalTime startAt) {
        this.startAt = startAt;
    }

    private Long id;
    private final LocalTime startAt;

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
}
