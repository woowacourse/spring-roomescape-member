package roomescape.reservationtime.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.model.ReservationTime;

public interface ReservationTimeRepository {

    Long save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long timeId);

    void deleteById(Long id);
}
