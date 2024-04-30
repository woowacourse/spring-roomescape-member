package roomescape.repository;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequest;

public interface ReservationRepository {
    Reservation save(ReservationRequest reservationRequest);

    List<Reservation> findAll();

    void delete(long id);
}
