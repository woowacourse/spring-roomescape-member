package roomescape.domain.reservation.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.entity.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);
}
