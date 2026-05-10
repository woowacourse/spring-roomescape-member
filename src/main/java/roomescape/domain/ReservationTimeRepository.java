package roomescape.domain;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    Long save(ReservationTime time);

    void deleteById(Long id);

    boolean existsById(Long id);

    default ReservationTime getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 ID입니다."));
    }
}
