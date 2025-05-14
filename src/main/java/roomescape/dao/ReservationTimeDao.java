package roomescape.dao;

import java.util.List;
import roomescape.entity.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

    ReservationTime create(ReservationTime time);

    void deleteById(Long id);

    boolean existsByStartAt(ReservationTime reservationTime);
}
