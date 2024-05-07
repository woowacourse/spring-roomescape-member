package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import roomescape.model.ReservationTime;

@Repository
public class ReservationTimeRepositoryImpl {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeRepositoryImpl(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeDao.findAllReservationTimes();
    }

    public ReservationTime addReservationTime(ReservationTime reservationTime) {
        return reservationTimeDao.addReservationTime(reservationTime);
    }

    public Long countReservationTimeByStartAt(LocalTime startAt) {
        return reservationTimeDao.countReservationTimeByStartAt(startAt);
    }

    public ReservationTime findReservationById(long timeId) {
        return reservationTimeDao.findReservationById(timeId);
    }

    public List<ReservationTime> findAllReservedTimes(LocalDate date, Long themeId) {
        return reservationTimeDao.findAllReservedTimes(date, themeId);
    }

    public void deleteReservationTime(Long timeId) {
        reservationTimeDao.deleteReservationTime(timeId);
    }

    public Long countReservationTime(Long timeId) {
        return reservationTimeDao.countReservationTimeById(timeId);
    }

    public Long countReservedTime(Long timeId) {
        return reservationDao.countReservationByTimeId(timeId);
    }

}
