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
        return reservationDao.save(theme, name, date, time);
    }

    public void delete(long id) {
        reservationDao.delete(id);
    }

    public int countByTimeId(long timeId) {
        return reservationDao.countByTimeId(timeId);
    }

}
