package roomescape.domain.reservationtime.repository;

import roomescape.domain.reservationtime.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime time);

    void deleteById(Long id);

    boolean existsById(Long id);

    Optional<ReservationTime> findById(Long id);
}
