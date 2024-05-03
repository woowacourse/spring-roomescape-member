package roomescape.reservation.domain;

import java.time.LocalTime;

public class ReservationTime {

    private Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        validateStartAt(startAt);
        this.startAt = startAt;
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시간 값이 입력되지 않았습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
