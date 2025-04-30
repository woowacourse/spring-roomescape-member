package roomescape.reservationTime.repository;

import java.util.List;
import roomescape.reservationTime.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime add(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    ReservationTime findByIdOrThrow(Long id);

    void delete(Long id);

    Long insertWithKeyHolder(ReservationTime reservationTime);
}
