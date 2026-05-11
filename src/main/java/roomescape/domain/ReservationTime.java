package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ReservationTime {
    public static final String TIME_SHOULD_NOT_BE_NULL = "시간을 입력해야 합니다.";
    private static final String INVALID_TIME_FORMAT = "시간 형식이 올바르지 않습니다. (HH:mm)";
    private final long id;
    private final LocalTime startAt;

    private ReservationTime(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime of(long id, String startAt) {
        validateIsNull(startAt);
        try {
            return new ReservationTime(id, LocalTime.parse(startAt));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(INVALID_TIME_FORMAT);
        }
    }

    public static ReservationTime of(long id, LocalTime startAt) {
        validateIsNull(startAt);
        return new ReservationTime(id, startAt);
    }

    public static ReservationTime of(String startAt) {
        validateIsNull(startAt);
        try {
            return new ReservationTime(0L, LocalTime.parse(startAt));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(INVALID_TIME_FORMAT);
        }
    }

    private static void validateIsNull(Object startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException(TIME_SHOULD_NOT_BE_NULL);
        }
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
