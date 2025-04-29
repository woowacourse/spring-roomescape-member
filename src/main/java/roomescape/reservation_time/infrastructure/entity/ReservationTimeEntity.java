package roomescape.reservation_time.infrastructure.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationTimeEntity {

    private final Long id;
    private final Time time;

    public static ReservationTimeEntity of(final Long id, final Time time) {
        return new ReservationTimeEntity(id, time);
    }
}
