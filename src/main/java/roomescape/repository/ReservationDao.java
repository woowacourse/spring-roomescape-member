package roomescape.repository;

import java.time.LocalDate;
import java.util.List;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public interface ReservationDao {
    List<Reservation> getAllReservations();

    Reservation addReservation(Reservation reservation);

    void deleteReservation(long id);

    long countReservationById(long id);

    long countReservationByTimeId(long timeId);

    long countReservationByDateAndTimeId(LocalDate date, long timeId, long themeId);

    List<ReservationTime> findReservationTimeByDateAndTheme(LocalDate date, long themeId);
}
