package roomescape.reservation.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.time.dao.TimeDao;
import roomescape.time.domain.Time;

@Repository
public class ReservationRepository {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;

    public ReservationRepository(ReservationDao reservationDao, TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public Reservation saveReservation(Reservation reservation) {
        Time time = timeDao.findById(reservation.getReservationTime().getId());
        reservation.setTime(time);
        return reservationDao.save(reservation);
    }

    public List<Reservation> findAllReservation() {
        return reservationDao.findAllOrderByDateAndReservationTime();
    }

    public int countReservationByTimeId(long timeId) {
        return reservationDao.findByTimeId(timeId);
    }

    public void deleteReservationById(long reservationId) {
        reservationDao.deleteById(reservationId);
    }
}
