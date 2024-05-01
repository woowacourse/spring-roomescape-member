package roomescape.domain;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> hasNotId(List<Long> unavailableTimeIds);

    boolean existsByStartAt(LocalTime startAt);

    ReservationTime save(ReservationTime time);

    void delete(ReservationTime time);
}
