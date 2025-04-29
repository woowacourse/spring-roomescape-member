package roomescape.repository;

import java.util.List;
import roomescape.domain.ReservationTime;

public interface RoomescapeTimeRepository {

    ReservationTime findById(Long timeId);

    List<ReservationTime> findAll();

    ReservationTime save(final ReservationTime reservationTime);

    int deleteById(final long id);
}
