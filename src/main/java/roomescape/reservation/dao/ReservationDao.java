package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationDao {

    Reservation save(Reservation reservation);

    List<Reservation> findAllReservations();

    List<Reservation> findReservationsByDateAndThemeId(LocalDate date, Long themeId);

    void delete(Long id);
}
