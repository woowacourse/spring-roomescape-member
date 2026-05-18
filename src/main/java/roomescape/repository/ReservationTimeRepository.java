package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.common.exception.ResourceNotFoundException;
import roomescape.entity.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    Long save(ReservationTime time);

    void deleteById(Long id);

    boolean existsById(Long id);

    default ReservationTime getById(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 예약 시간입니다."));
    }
}
