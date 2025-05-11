package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.model.Reservation;

public interface ReservationRepository {

    List<Reservation> getAllReservations();

    Reservation addReservation(Reservation reservation);

    int deleteReservation(Long id);

    List<Reservation> findBy(Long memberId, Long themeId, LocalDate fromDate, LocalDate toDate);
}
