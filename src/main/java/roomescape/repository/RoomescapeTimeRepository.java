package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface RoomescapeTimeRepository {

    Optional<ReservationTime> findById(final Long timeId);

    List<ReservationTime> findAll();

    ReservationTime save(final ReservationTime time);

    boolean deleteById(final Long id);
}
