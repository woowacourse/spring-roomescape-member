package roomescape.reservation.application.repository;

import java.time.LocalDateTime;
import java.util.List;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.domain.aggregate.Reservation;

public interface ReservationRepository {
    Reservation insert(CreateReservationRequest request);

    List<Reservation> findAllReservations();

    void delete(Long id);

    boolean existsByTimeId(Long timeId);

    boolean existsByDateTime(LocalDateTime reservationDateTime);
}
