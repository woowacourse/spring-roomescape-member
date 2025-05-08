package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    Long saveAndReturnId(Reservation reservation);

    int deleteById(Long id);

    Optional<Reservation> findById(Long reservationId);

    List<Reservation> findAll();

    List<Reservation> findAllByTimeId(Long timeId);

    List<Reservation> findAllByThemeId(Long themeId);

}
