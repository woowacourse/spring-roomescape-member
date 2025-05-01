package roomescape.business.model.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.business.model.entity.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    boolean existByTime(LocalTime time);
}
