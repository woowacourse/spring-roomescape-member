package roomescape.reservation.application;

import java.util.List;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation insert(CreateReservationRequest request);

    List<Reservation> findAllReservations();

    void delete(Long id);
}
