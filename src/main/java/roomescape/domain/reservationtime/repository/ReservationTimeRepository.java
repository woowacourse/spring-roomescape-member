package roomescape.domain.reservationtime.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservationtime.entity.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    ReservationTime save(ReservationTime reservationTime);

    int update(Long id, ReservationTime reservationTime);

    int deleteById(Long id);

    boolean existsByStartAt(LocalTime startAt);

    boolean existsByStartAtAndIdNot(LocalTime startAt, Long id);
}
