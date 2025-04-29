package roomescape.dao;

import java.util.List;
import roomescape.model.Reservation;

public interface ReservationDao {
    public Reservation save(Reservation reservation);
    public boolean deleteById(Long id);
    public List<Reservation> findAll();
}
