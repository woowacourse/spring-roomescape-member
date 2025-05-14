package roomescape.reservation.dao;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void delete(Long id);

    Optional<Reservation> findByThemeAndDateAndTime(Reservation reservation);

    Optional<Reservation> findByTimeId(Long timeId);

    Optional<Reservation> findByThemeId(Long themeId);

    boolean existById(Long id);
}
