package roomescape.reservationTime.repository;

import java.util.List;
import roomescape.reservationTime.domain.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime add(ReservationTime reservationTime);

    ReservationTime findByIdOrThrow(Long id);

    void delete(Long id);

    Long insertWithKeyHolder(ReservationTime reservationTime);
}
