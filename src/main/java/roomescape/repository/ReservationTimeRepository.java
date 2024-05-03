package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;
import roomescape.repository.dao.ReservationDao;
import roomescape.repository.dao.ReservationTimeDao;

import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservationTimeRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeRepository(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeDao.findAllReservationTimes();
    }

    public Long countReservationTimeByStartAt(LocalTime startAt) {
        return reservationTimeDao.countReservationTimeByStartAt(startAt);
    }

    public ReservationTime saveReservationTime(ReservationTime reservationTime) {
        return reservationTimeDao.saveReservationTime(reservationTime);
    }

    public ReservationTime findReservationById(long id) {
        return reservationTimeDao.findReservationTimeById(id);
    }

    public Long countReservationTimeById(long id) {
        return reservationTimeDao.countReservationTimeById(id);
    }

    public Long countReservationByTimeId(long timeId) {
        return reservationDao.countReservationByTimeId(timeId);
    }

    public void deleteReservationTimeById(long id) {
        reservationTimeDao.deleteReservationTimeById(id);
    }
}
