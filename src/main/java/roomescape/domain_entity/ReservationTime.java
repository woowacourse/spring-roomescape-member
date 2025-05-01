package roomescape.domain_entity;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime copyWithId(Long id) {
        return new ReservationTime(id, startAt);
    }

    public boolean isPastTime() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        return startAt.isBefore(now);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
