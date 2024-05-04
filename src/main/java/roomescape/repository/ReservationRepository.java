package roomescape.repository;

import java.time.LocalDate;
import java.util.List;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public interface ReservationRepository {
    List<Reservation> getAllReservations();

    Reservation addReservation(Reservation reservation);

    void deleteReservation(long id);

    Long countReservationById(long id);

    Long countReservationByTimeId(long timeId);

    Long countReservationByDateAndTimeId(LocalDate date, long timeId);

    List<ReservationTime> findReservationTimeByDateAndTheme(LocalDate date, long themeId);
}
