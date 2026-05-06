package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

@Repository
public class ReservationRepository {
    private final ReservationDao reservationDao;

    public ReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(Theme theme, String name, LocalDate date, ReservationTime time) {
        return reservationDao.save(name, date, time, theme);
    }

    public void delete(long id) {
        reservationDao.delete(id);
    }

    public int countByTimeId(long timeId) {
        return reservationDao.countByTimeId(timeId);
    }

    public Reservation findById(long id) {
        return reservationDao.findById(id);
    }
}
