package roomescape.entity;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeEntity(Long id, LocalTime startAt) {
    public static ReservationTimeEntity of(Long id, ReservationTime time) {
        return new ReservationTimeEntity(id, time.getStartAt());
    }

    public ReservationTime toDomain() {
        ReservationTime time = new ReservationTime(this.startAt);
        return time.withId(this.id);
    }
}
