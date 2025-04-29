package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    Long saveAndReturnId(Reservation reservation);

    int deleteById(Long id);

    List<Reservation> findAll();

    List<Reservation> findAllByTimeId(Long timeId);

    Boolean existByDateAndTimeId(LocalDate date, Long timeId);
}
