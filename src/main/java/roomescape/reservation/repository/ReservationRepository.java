package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.entity.ReservationEntity;

public interface ReservationRepository {

    Long save(ReservationEntity reservationEntity);

    Optional<Reservation> findById(Long id);

    Long countByTimeId(Long timeId);

    List<Reservation> findAll();

    void deleteById(Long id);
}
