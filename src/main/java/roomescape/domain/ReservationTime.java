package roomescape.domain;

import java.time.LocalTime;

public record ReservationTime(Long id, LocalTime startAt) {

    public ReservationTime {
        validate(startAt);
    }

    public static ReservationTime transientOf(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간은 필수입니다.");
        }
    }
}
