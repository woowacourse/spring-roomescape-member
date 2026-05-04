package roomescape.domain.reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    int deleteById(Long id);

    int countByTimeId(Long timeId);
}
