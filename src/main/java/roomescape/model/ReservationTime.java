package roomescape.model;

import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private static void validateStartAt(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시간이 비어 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
