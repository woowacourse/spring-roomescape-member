package roomescape.reservation.mapper;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.entity.ReservationEntity;
import roomescape.theme.mapper.ThemeMapper;
import roomescape.theme.repository.entity.ThemeEntity;
import roomescape.time.mapper.ReservationTimeMapper;
import roomescape.time.repository.entity.ReservationTimeEntity;

public class ReservationMapper {

    private ReservationMapper() {
    }

    public static Reservation toReservation(ReservationEntity reservationEntity,
                                            ReservationTimeEntity reservationTimeEntity, ThemeEntity themeEntity) {
        return new Reservation(reservationEntity.getId(), reservationEntity.getName(), reservationEntity.getDate(),
                ReservationTimeMapper.toReservationTime(reservationTimeEntity),
                ThemeMapper.toTheme(themeEntity));
    }

}
