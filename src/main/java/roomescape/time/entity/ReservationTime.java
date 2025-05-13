package roomescape.time.entity;

import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public boolean equalsTime(ReservationTime time) {
        return this.startAt.equals(time.getStartAt());
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 시간입니다.");
        }
    }
}
