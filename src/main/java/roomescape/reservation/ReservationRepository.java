package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.ReservationTime;

@Repository
public class ReservationRepository {
    private final ReservationDao reservationDao;

    public ReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(String name, LocalDate date, ReservationTime time) {
        return reservationDao.save(name, date, time);
    }

    public void delete(long id) {
        reservationDao.delete(id);
    }

    public int countByTimeId(long timeId) {
        return reservationDao.countByTimeId(timeId);
    }
}
