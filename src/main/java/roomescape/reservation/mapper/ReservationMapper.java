package roomescape.reservation.mapper;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.entity.ReservationEntity;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.entity.ThemeEntity;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.entity.ReservationTimeEntity;

public class ReservationMapper {

    private ReservationMapper() {
    }

    public static Reservation toReservation(ReservationEntity reservationEntity,
                                            ReservationTimeEntity reservationTimeEntity, ThemeEntity themeEntity) {
        return new Reservation(reservationEntity.getId(), reservationEntity.getName(), reservationEntity.getDate(),
                new ReservationTime(reservationTimeEntity.getId(), reservationTimeEntity.getStartAt()),
                new Theme(themeEntity.getId(), themeEntity.getName(), themeEntity.getDescription(),
                        themeEntity.getImageUrl()));
    }

}
