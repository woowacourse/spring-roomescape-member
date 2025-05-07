package roomescape.persistence;

import java.util.List;
import java.util.Optional;
import roomescape.business.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Long add(Reservation reservation);

    void deleteById(Long id);

    boolean existsByReservation(Reservation reservation);

    boolean existsByTimeId(Long timeId);

    boolean existByThemeId(Long id);
}
