package roomescape.domain;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    boolean existsByStartAt(ReservationTime reservationTime);
}
