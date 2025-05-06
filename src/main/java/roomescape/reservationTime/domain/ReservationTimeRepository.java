package roomescape.reservationTime.domain;

import java.util.List;

public interface ReservationTimeRepository {

    Long save(ReservationTime reservationTime);

    boolean deleteBy(Long id);

    ReservationTime findBy(Long id);

    List<ReservationTime> findAll();
}
