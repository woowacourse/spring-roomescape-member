package roomescape.domain.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface TimeRepository {
    Long save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

   Optional<ReservationTime> findById(Long id);

    void deleteById(Long id);
}
