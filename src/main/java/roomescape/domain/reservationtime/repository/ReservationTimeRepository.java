package roomescape.domain.reservationtime.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime time);

    void deleteById(Long id);

    boolean existsById(Long id);

    Optional<ReservationTime> findById(Long id);
}
