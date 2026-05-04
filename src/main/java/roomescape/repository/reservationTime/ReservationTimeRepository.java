package roomescape.repository.reservationTime;

import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime createReservationTime(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);
}
