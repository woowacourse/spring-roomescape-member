package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationDao {

    Reservation save(Reservation reservation);

    List<Reservation> findAllOrderByDateAndTime();

    List<Reservation> findAllByThemeIdAndDate(long themeId, LocalDate date);

    void deleteById(long reservationId);

    int countByTimeId(long timeId);

}
