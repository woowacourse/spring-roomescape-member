package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

public interface ReservationDao {

    List<Reservation> findAll();

    long save(Reservation reservation);

    void delete(Long id);

    boolean existReservationByTime(Long id);

    boolean existReservationByTheme(Long id);

    boolean existReservationOf(ReservationDate date, Long timeId);

    Optional<Reservation> findById(Long id);
}
