package roomescape.domain;

import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validate(id, startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validate(Long id, LocalTime startAt) {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] id는 비어 있을 수 없습니다.");
        }
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 예약 시간은 비어 있을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
