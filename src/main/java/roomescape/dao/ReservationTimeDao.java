package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.dto.ReservationTimeResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public interface ReservationTimeDao {
    ReservationTime save(ReservationTime time);
    boolean deleteById(Long id);
    List<ReservationTime> findAll();
    Optional<ReservationTime> findById(Long id);
}
