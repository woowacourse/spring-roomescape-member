package roomescape.repository;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

public interface ReservationRepository {

    List<Reservation> findAllReservation();

    void saveReservation(Reservation reservation);

    void deleteReservation(Long id);

    boolean hasAnotherReservation(ReservationDate date, Long timeId);

    Reservation findById(Long id);

}
