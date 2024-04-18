package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;

public interface ReservationTimeDao {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    ReservationTime insert(ReservationTimeAddRequest reservationTimeAddRequest);

    void deleteById(Long id);
}
