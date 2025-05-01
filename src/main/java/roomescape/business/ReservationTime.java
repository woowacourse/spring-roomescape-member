package roomescape.business;

import java.time.LocalTime;

public final class ReservationTime {

    private Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this.startAt = startAt;
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

    public void setId(Long id) {
        this.id = id;
    }
}
