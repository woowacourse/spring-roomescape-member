package roomescape.repository;

import roomescape.entity.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeDao {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    boolean isExistByTime(LocalTime time);
}
