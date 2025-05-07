package roomescape.reservationtime.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime findByIdOrThrow(Long id);

    Optional<ReservationTime> findById(Long id);

    ReservationTime add(ReservationTime reservationTime);

    void delete(Long id);

    Long insertWithKeyHolder(ReservationTime reservationTime);
}
