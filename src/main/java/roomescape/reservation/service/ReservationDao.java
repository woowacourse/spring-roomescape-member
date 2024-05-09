package roomescape.reservation.service;

import roomescape.reservation.domain.Reservation;
import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {

    Reservation save(Reservation reservation);

    List<Reservation> findAllReservations();

    List<Reservation> findReservationsByDateAndThemeId(LocalDate date, Long themeId);

    void delete(Long id);
}
