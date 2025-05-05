package roomescape.reservation.dao;

import java.time.LocalDate;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;

public interface ReservationDao extends Dao<Reservation> {
    Boolean existsByDateAndTimeId(LocalDate date, Long timeId);
}
