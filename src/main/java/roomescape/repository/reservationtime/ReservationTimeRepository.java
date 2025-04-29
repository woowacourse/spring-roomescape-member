package roomescape.repository.reservationtime;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime add(ReservationTime time);

    List<ReservationTime> findAll();

    int deleteById(Long id);

    Optional<ReservationTime> findById(Long timeId);
}
