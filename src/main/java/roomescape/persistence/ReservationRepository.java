package roomescape.persistence;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.reservation.Reservation;
import roomescape.presentation.admin.dto.ReservationQueryCondition;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Reservation add(Reservation reservation);

    void deleteById(Long id);

    boolean existsByReservation(Reservation reservation);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long id);

    List<Reservation> findAllByCondition(ReservationQueryCondition condition);
}
