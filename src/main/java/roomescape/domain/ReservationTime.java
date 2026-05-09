package roomescape.domain;

import java.time.LocalTime;

public class ReservationTime {
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        validate(startAt);
        this.startAt = startAt;
    }

    private static void validate(LocalTime startAt) {
        validateStartAt(startAt);
    }

    private static void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시간은 비어 있을 수 없습니다.");
        }
    }
    
    public LocalTime getStartAt() {
        return startAt;
    }
}
