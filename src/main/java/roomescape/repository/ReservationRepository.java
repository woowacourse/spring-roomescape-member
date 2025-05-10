package roomescape.repository;

import java.util.List;
import java.util.Map;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> readAll();

    void delete(Long id);

    boolean existsByTimeId(Long id);

    List<Reservation> readAllWithFilter(Map<String, Object> filter);
}
