package roomescape.domain;

import java.time.LocalTime;

// TODO: record로 변경?
public record ReservationTime(
        Long id,
        LocalTime startAt
) {

    public ReservationTime {
        validateNotNull(startAt);
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    private void validateNotNull(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시간은 null이 될 수 없습니다.");
        }
    }
}
