package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {


    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Long> getPopularThemeIds();

    List<Reservation> findByTimeId(Long timeId);

    void delete(Long id);

    boolean existByTimeId(Long timeId);
}
