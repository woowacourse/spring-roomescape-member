package roomescape.domain.reservation.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.model.Reservation;
import roomescape.domain.reservation.model.ReservationDate;

public interface ReservationDao {

    List<Reservation> findAll();

    long save(Reservation reservation);

    boolean delete(Long id);

    boolean existReservationByTime(Long id);

    boolean existReservationByTheme(Long id);

    boolean existReservationOf(ReservationDate date, Long themeId, Long timeId);

    Optional<Reservation> findById(Long id);

    List<Reservation> findOf(String dateFrom, String dateTo, Long memberId, Long themeId);
}
