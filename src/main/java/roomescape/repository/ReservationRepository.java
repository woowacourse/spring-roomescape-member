package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.dao.ReservationDao;
import roomescape.repository.dao.ReservationTimeDao;
import roomescape.repository.dao.ThemeDao;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationRepository(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAllReservations() {
        return reservationDao.findAllReservations();
    }

    public ReservationTime findReservationTimeById(long id) {
        return reservationTimeDao.findReservationTimeById(id);
    }

    public Theme findThemeById(long id) {
        return themeDao.findThemeById(id);
    }

    public Long countReservationByDateAndTimeId(LocalDate date, long timeId) {
        return reservationDao.countReservationByDateAndTimeId(date, timeId);
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationDao.saveReservation(reservation);
    }

    public Long countReservationById(long id) {
        return reservationDao.countReservationById(id);
    }

    public void deleteReservationById(long id) {
        reservationDao.deleteReservationById(id);
    }

    public List<ReservationTime> findReservationTimeBooked(LocalDate date, long themeId) {
        return reservationDao.findReservationTimeBooked(date, themeId);
    }

    public List<ReservationTime> findReservationTimeNotBooked(LocalDate date, long themeId) {
        return reservationDao.findReservationTimeNotBooked(date, themeId);
    }
}
