package roomescape.time.domain;

import java.time.LocalTime;

public record Time(Long id, LocalTime startAt) {

    private static Long NOT_SAVED_ID = 0L;

    public static Time createBeforeSaved(LocalTime startAt) {
        return new Time(NOT_SAVED_ID, startAt);
    }

    public Time {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] id가 null이어서는 안 됩니다.");
        }
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 시작 시간이 null이어서는 안 됩니다.");
        }
    }

    public boolean isBefore(LocalTime targetTime) {
        return startAt.isBefore(targetTime);
    }

}
