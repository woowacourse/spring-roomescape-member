package roomescape.domain.time;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, ReservationTime time) {
        this(id, time.startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        Objects.requireNonNull(startAt, "예약 시간은 필수입니다.");
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
