package roomescape.repository;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAllReservations();

    Reservation saveReservation(Reservation reservation);

    void deleteReservationById(long id);

    Long countReservationById(long id);

    Long countReservationByTimeId(long timeId);

    Long countReservationByDateAndTimeId(LocalDate date, long timeId);

    List<ReservationTime> findReservationTimeByDateAndThemeId(LocalDate date, long themeId);
}
