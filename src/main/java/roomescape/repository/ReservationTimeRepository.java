package roomescape.repository;

import java.util.List;
import java.util.Optional;

import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> getAll();

    Optional<ReservationTime> findById(Long id);

    void remove(ReservationTime reservation);
}
