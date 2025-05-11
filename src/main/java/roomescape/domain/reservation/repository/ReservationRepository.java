package roomescape.domain.reservation.repository;

import java.util.List;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;

public interface ReservationRepository {

    List<Reservation> findAll();

    void save(Reservation reservation);

    void delete(Long id);

    boolean hasAnotherReservation(ReservationDate date, Long timeId);

    Reservation findById(Long id);

    List<Reservation> findOf(String dateFrom, String dateTo, Long memberId, Long themeId);
}
