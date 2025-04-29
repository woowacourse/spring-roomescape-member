package roomescape.reservation.domain;

import roomescape.reservation_time.domain.ReservationTimeId;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    boolean existsById(ReservationId id);

    boolean existsByTimeId(ReservationTimeId timeId);

    Optional<Reservation> findById(ReservationId id);

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(ReservationId id);
}
