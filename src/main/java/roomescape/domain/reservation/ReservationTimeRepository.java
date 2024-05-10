package roomescape.domain.reservation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    default ReservationTime getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("존재하지 않는 시간입니다. timeId: %d", id)));
    }

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    void deleteById(Long id);
}
