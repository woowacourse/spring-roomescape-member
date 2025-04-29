package roomescape.reservation.application.usecase;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation_time.domain.ReservationTimeId;

import java.util.List;

public interface ReservationQueryUseCase {

    List<Reservation> getAll();

    boolean existsByTimeId(ReservationTimeId timeId);
}
