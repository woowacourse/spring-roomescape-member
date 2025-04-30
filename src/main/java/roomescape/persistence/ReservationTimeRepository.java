package roomescape.persistence;

import java.util.List;
import roomescape.business.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

    Long add(ReservationTime reservationTime);

    void deleteById(Long id);
}
