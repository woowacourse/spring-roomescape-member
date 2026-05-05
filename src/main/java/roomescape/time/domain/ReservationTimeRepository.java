package roomescape.time.domain;

import java.util.List;
import java.util.Optional;
import roomescape.time.domain.exception.ReservationTimeNotFoundException;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);
    List<ReservationTime> findAll();
    Optional<ReservationTime> findById(Long id);
    int deleteById(Long id);

    default ReservationTime getById(Long id) {
        return findById(id).orElseThrow(() -> new ReservationTimeNotFoundException("존재하지 않는 시간ID 입니다."));
    }
}
