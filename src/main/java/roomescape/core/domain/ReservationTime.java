package roomescape.core.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ReservationTime {
    private Long id;
    private LocalTime startAt;

    public ReservationTime() {
    }

    public ReservationTime(final String startAt) {
        this(null, startAt);
    }

    public ReservationTime(final Long id, final String startAt) {
        this.id = id;
        this.startAt = parseStartAt(startAt);
    }

    private LocalTime parseStartAt(final String startAt) {
        try {
            return LocalTime.parse(startAt);
        } catch (final DateTimeParseException e) {
            throw new IllegalArgumentException("시간 형식이 잘못되었습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public boolean isPast() {
        return startAt.isBefore(LocalTime.now());
    }
}
