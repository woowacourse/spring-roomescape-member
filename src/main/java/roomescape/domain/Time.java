package roomescape.domain;

import java.time.LocalTime;

public record Time(Long id, LocalTime startAt) {

    public Time {
        validate(startAt);
    }

    public static Time transientOf(LocalTime startAt) {
        return new Time(null, startAt);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간은 필수입니다.");
        }
    }
}
