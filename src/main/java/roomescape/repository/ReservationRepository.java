package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.repository.criteria.ReservationCriteria;

public interface ReservationRepository {

    long insert(Reservation reservation);

    boolean existSameReservation(LocalDate date, long timeId, long themeId);

    List<Reservation> findAll();

    List<Reservation> findAllByCriteria(ReservationCriteria criteria);

    Optional<Reservation> findById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    boolean deleteById(long id);
}
