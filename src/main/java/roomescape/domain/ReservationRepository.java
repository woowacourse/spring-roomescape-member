package roomescape.domain;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);
}
