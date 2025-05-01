package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import roomescape.entity.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime findById(Long id);

    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);

    boolean existsByStartAt(LocalTime time);
}
