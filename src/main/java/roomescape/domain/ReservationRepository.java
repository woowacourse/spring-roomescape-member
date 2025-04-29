package roomescape.domain;

import java.util.List;
import java.util.Optional;


public interface ReservationRepository {

    List<Reservation> findAll();

    Long create(Reservation reservation);

    void deleteById(Long reservationId);

    Optional<Reservation> findById(Long reservationId);

    boolean existByTimeId(Long reservationTimeId);
}
