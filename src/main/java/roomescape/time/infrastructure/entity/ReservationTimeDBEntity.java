package roomescape.time.infrastructure.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

import java.sql.Time;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationTimeDBEntity {

    private final Long id;
    private final Time time;

    public static ReservationTimeDBEntity of(final Long id, final Time time) {
        return new ReservationTimeDBEntity(id, time);
    }

    public ReservationTime toDomain() {
        return ReservationTime.withId(
                ReservationTimeId.from(id),
                time.toLocalTime());
    }
}
