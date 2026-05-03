package roomescape.time.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime create(String startAt) {
        return new ReservationTime(null, parseLocalTime(startAt));
    }

    private static LocalTime parseLocalTime(String time) {
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String.format("시간 형식이 올바르지 않습니다. (입력값: %s)", time));
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
