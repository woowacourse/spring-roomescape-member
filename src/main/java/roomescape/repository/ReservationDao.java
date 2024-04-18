package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationAddRequest;

public interface ReservationDao {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Reservation insert(ReservationAddRequest reservationAddRequest);

    void deleteById(Long id);
}
