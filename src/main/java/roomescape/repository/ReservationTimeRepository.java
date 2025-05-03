package roomescape.repository;

import java.util.List;
import roomescape.entity.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime findById(Long id);

    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);
}
