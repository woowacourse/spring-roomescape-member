package roomescape.domain;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    long countByTimeId(Long id);
}
