package roomescape.entity;

import java.time.LocalTime;

public record ReservationTime(Long id, LocalTime startAt) {

    public ReservationTime {
        validate(startAt);
    }

    public void isBefore() {
        if (startAt.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("[ERROR] 예약이 불가능한 시간입니다: " + startAt);
        }
    }

    public boolean equalsTime(ReservationTime time) {
        return this.startAt == time.startAt();
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 시간입니다.");
        }
    }
}
