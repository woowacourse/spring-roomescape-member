package roomescape.repository;

import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationTime.CreateReservationTimeRequest;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

    Long save(ReservationTime reservationTime);

    void deleteById(Long id);
}
