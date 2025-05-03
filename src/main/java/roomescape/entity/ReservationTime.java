package roomescape.entity;

import java.time.LocalTime;

public record ReservationTime(Long id, LocalTime startAt) {

    public ReservationTime {
        validate(startAt);
    }

    public boolean equalsTime(ReservationTime time) {
        return this.startAt.equals(time.startAt());
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 시간입니다.");
        }
    }
}
