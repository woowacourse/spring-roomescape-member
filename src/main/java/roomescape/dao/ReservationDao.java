package roomescape.dao;

import java.util.List;
import roomescape.model.Reservation;

public interface ReservationDao {
    Reservation save(Reservation reservation);
    boolean deleteById(Long id);
    List<Reservation> findAll();
}
