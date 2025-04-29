package roomescape.reservation.infrastructure.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.reservation_time.infrastructure.entity.ReservationTimeEntity;

import java.sql.Date;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationEntity {

    private final Long id;
    private final String name;
    private final Date date;
    private final ReservationTimeEntity time;

    public static ReservationEntity of(final Long id,
                                       final String name,
                                       final Date date,
                                       final ReservationTimeEntity timeEntity) {
        return new ReservationEntity(id, name, date, timeEntity);
    }
}
