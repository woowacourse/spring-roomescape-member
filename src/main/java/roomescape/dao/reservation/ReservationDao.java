package roomescape.dao.reservation;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void delete(Long id);

    Optional<Reservation> findByThemeAndDateAndTime(Reservation reservation);
}
