package roomescape.reservation.entity;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.entity.ReservationTimeEntity;
import roomescape.theme.entity.ThemeEntity;

public record ReservationEntity(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeEntity timeEntity,
        ThemeEntity themeEntity
) {
    public ReservationEntity(final Long id, final String name, final String date,
                             final ReservationTimeEntity timeEntity, final ThemeEntity themeEntity) {
        this(id, name, LocalDate.parse(date), timeEntity, themeEntity);
    }

    public Reservation toReservation() {
        return Reservation.of(id, name, date, timeEntity.toReservationTime(), themeEntity.toTheme());
    }
}