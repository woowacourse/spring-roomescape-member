package roomescape.dao;

import roomescape.domain.Reservation;

import java.util.List;
import java.util.Optional;


public interface ReservationDao {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Long insert(Reservation reservation);

    int delete(Long id);
}
