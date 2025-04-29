package roomescape.repository.dao;

import java.util.List;
import java.util.Optional;

import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> selectAll();

    ReservationTime insertAndGet(ReservationTime reservationTime);

    Optional<ReservationTime> selectById(Long id);

    void deleteById(Long id);
}
