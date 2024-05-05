package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationTimeDao {
    ReservationTime save(final ReservationTime reservationTime);

    List<ReservationTime> getAll();

    Optional<ReservationTime> findById(final long id);

    void delete(final long id);
}
