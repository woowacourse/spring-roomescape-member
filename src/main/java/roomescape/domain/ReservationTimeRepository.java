package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    default ReservationTime getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."));
    }

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByStartAt(LocalTime startAt);
}
