package roomescape.time.domain;

import java.util.List;

public interface ReservationTimeRepository {

    Long save(final ReservationTime reservationTime);

    int deleteById(final Long id);

    ReservationTime findById(Long id);

    List<ReservationTime> findAll();
}
