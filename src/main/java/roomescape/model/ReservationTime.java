package roomescape.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReservationTime {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final String startAt) {
        this(id, parseTime(startAt));
    }

    public ReservationTime(final String startAt) {
        this(null, parseTime(startAt));
    }

    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    private static LocalTime parseTime(final String startAt) {
        if (startAt.isEmpty()) {
            throw new IllegalArgumentException("시간이 입력되지 않았습니다.");
        }
        return LocalTime.parse(startAt);
    }

    public String getFormattedTime() {
        return startAt.format(TIME_FORMATTER);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
