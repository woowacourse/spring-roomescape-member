package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.model.Reservation;

public interface ReservationDao {
    Reservation save(Reservation reservation);
    boolean deleteById(Long id);
    List<Reservation> findAll();
    int countByTimeId(Long timeId);
    boolean isExistByTimeIdAndDate(Long timeId, LocalDate date);
}
