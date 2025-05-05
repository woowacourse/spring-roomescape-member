package roomescape.domain.entity;

import java.time.LocalTime;

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
            throw new IllegalArgumentException("예약 시작시간은 null이 될 수 없습니다.");
        }
    }
}
