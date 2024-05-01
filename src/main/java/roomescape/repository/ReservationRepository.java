package roomescape.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    default Reservation getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약이 존재하지 않습니다."));
    }

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existsByTimeId(Long id);
}
