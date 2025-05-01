package roomescape.reservation.infrastructure.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.theme.infrastructure.entity.ThemeDBEntity;
import roomescape.time.infrastructure.entity.ReservationTimeDBEntity;

import java.sql.Date;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationDBEntity {

    private final Long id;
    private final String name;
    private final Date date;
    private final ReservationTimeDBEntity time;
    private final ThemeDBEntity theme;

    public static ReservationDBEntity of(final Long id,
                                         final String name,
                                         final Date date,
                                         final ReservationTimeDBEntity timeEntity,
                                         final ThemeDBEntity themeDBEntity) {
        return new ReservationDBEntity(id, name, date, timeEntity, themeDBEntity);
    }
}
