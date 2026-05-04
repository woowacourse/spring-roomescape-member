package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeDao.findById(timeId);
        Theme theme = themeDao.findById(themeId);
        Reservation reservation = new Reservation(name, date, time, theme);
        return reservationDao.save(reservation);
    }

    public void delete(Long id) {
        validateHasReservation(id);
        reservationDao.deleteById(id);
    }

    private void validateHasReservation(Long id) {
        boolean hasReservation = reservationDao.existByTimeId(id);
        if (!hasReservation) {
            throw new IllegalArgumentException("존재하지 않는 예약니다.");
        }
    }
}
