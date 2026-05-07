package roomescape.domain;

import java.time.LocalTime;

public class ReservationTime {
    public static final String TIME_SHOULD_NOT_BE_NULL = "시간을 입력해야 합니다.";
    private final long id;
    private final LocalTime startAt;

    private ReservationTime(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime of(long id, LocalTime startAt) {
        validateIsNull(startAt);
        return new ReservationTime(id, startAt);
    }

    public static ReservationTime of(LocalTime startAt) {
        validateIsNull(startAt);
        return new ReservationTime(0L, startAt);
    }

    public static ReservationTime of(String startAt) {
        validateIsNull(startAt);
        return new ReservationTime(0L, LocalTime.parse(startAt));
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
