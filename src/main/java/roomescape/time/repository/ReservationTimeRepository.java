package roomescape.time.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.time.domain.ReservationTime;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    Long save(ReservationTime reservationTime);

    void delete(Long id);

    boolean existsByStartAt(LocalTime startAt);
}
