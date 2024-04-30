package roomescape.repository.reservationtime;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    void delete(Long id);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findAll();
}
