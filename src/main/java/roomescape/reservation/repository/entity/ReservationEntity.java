package roomescape.reservation.repository.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.theme.repository.entity.ThemeEntity;
import roomescape.time.repository.entity.ReservationTimeEntity;

import java.sql.Date;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationEntity {

    private final Long id;
    private final String name;
    private final Date date;
    private final ReservationTimeEntity time;
    private final ThemeEntity theme;

    public static ReservationEntity of(final Long id,
                                       final String name,
                                       final Date date,
                                       final ReservationTimeEntity timeEntity,
                                       final ThemeEntity themeEntity) {
        return new ReservationEntity(id, name, date, timeEntity, themeEntity);
    }
}
