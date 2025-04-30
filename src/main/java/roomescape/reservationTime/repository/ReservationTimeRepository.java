package roomescape.reservationTime.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservationTime.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime add(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    ReservationTime findByIdOrThrow(Long id);

    Optional<ReservationTime> findById(Long id);

    void delete(Long id);

    Long insertWithKeyHolder(ReservationTime reservationTime);
}
