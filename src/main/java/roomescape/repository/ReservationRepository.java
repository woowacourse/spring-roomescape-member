package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.model.Reservation;

public interface ReservationRepository {

    List<Reservation> getReservations(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);

    Reservation addReservation(Reservation reservation);

    void deleteReservation(Long id);

}
