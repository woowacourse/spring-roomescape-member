package roomescape.domain;

import java.time.LocalTime;

public class ReservationTime {
    private Long id;
    private LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validateNotNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private static void validateNotNull(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시간 필드 값이 Null 입니다.");
        }
    }

    public boolean isMatch(LocalTime time) {
        return this.startAt.equals(time);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
