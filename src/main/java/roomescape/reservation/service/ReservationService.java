package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.dao.ReservationTimeDao;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.dao.ThemeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,ThemeDao themeDao, ReservationTimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.selectAll();
    }

    public List<Reservation> findByName(String name) {
        return reservationDao.selectByName(name);
    }

    public Reservation addReservation(String name, LocalDate date, Long timeId, Long themeId, LocalDateTime now) {

        if (!reservationTimeDao.existsById(timeId)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND);
        }

        if (!themeDao.existsById(themeId)) {
            throw new BusinessException(ErrorCode.THEME_NOT_FOUND);
        }

        ReservationTime time = reservationTimeDao.selectById(timeId);
        Reservation reservation = new Reservation(name, date, time, themeId);

        if (reservation.isPast(now)) {
            throw new BusinessException(ErrorCode.RESERVATION_CREATE_TO_PAST);
        }

        boolean isAvailable = reservationDao.isAvailable(themeId, date, timeId);

        if (!isAvailable) {
            throw new BusinessException(ErrorCode.RESERVATION_CONFLICT);
        }

        return reservationDao.insert(reservation);
    }

    public void update(Long id, String name, LocalDate date, Long timeId, LocalDateTime now) {
        Reservation reservation = reservationDao.selectById(id);
        ReservationTime time = reservationTimeDao.selectById(timeId);

        if (date.isBefore(now.toLocalDate()) || (date.equals(now.toLocalDate()) && time.isBefore(now.toLocalTime()))) {
            throw new BusinessException(ErrorCode.RESERVATION_UPDATE_TO_PAST);
        }

        validateReservationOwnerAndTime(name, now, reservation);

        if (!reservationDao.isAvailable(reservation.getThemeId(), date, timeId)) {
            throw new BusinessException(ErrorCode.RESERVATION_CONFLICT);
        }

        reservationDao.update(id, name, date, timeId);
    }

    public void delete(Long id, String name, LocalDateTime now) {
        Reservation reservation = reservationDao.selectById(id);

        validateReservationOwnerAndTime(name, now, reservation);
        reservationDao.delete(id, name);
    }

    private void validateReservationOwnerAndTime(String name, LocalDateTime now, Reservation reservation) {
        if (!reservation.getName().equals(name)) {
            throw new BusinessException(ErrorCode.RESERVATION_FORBIDDEN);
        }

        if (reservation.isPast(now)) {
            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_PAST);
        }
    }
}
