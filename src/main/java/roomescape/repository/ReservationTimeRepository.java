package roomescape.repository;

import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(long id);

    ReservationTime save(ReservationTime reservationTime);

    int delete(long id);
}
