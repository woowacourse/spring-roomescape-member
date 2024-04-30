package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    boolean deleteById(Long id);
}
