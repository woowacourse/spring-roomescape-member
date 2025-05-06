package roomescape.entity;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = Objects.requireNonNull(startAt);
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

    public Long getId() {
        if (id == null) {
            throw new NullPointerException("id값이 존재하지 않습니다.");
        }
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
