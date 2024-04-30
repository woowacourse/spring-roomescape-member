package roomescape.core.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ReservationTime {
    private Long id;
    private String startAt;

    public ReservationTime() {
    }

    public ReservationTime(final String startAt) {
        this(null, startAt);
    }

    public ReservationTime(final Long id, final String startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateStartAt(final String startAt) {
        try {
            LocalTime.parse(startAt);
        } catch (final DateTimeParseException e) {
            throw new IllegalArgumentException("시간 형식이 잘못되었습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
