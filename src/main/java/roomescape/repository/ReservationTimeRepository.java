package roomescape.repository;

import roomescape.model.ReservationTime;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(final ReservationTime reservationTime);

    Optional<ReservationTime> findById(final Long id);

    List<ReservationTime> findAll();

    void deleteById(final Long id);
}
