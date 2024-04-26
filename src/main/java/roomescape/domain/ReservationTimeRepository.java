package roomescape.domain;

import java.util.List;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    ReservationTime findById(Long id);

    void deleteById(Long id);

    List<ReservationTime> findAll();
}
