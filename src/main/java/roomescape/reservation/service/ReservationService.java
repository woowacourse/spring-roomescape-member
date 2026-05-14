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

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.selectAll();
    }

    public List<Reservation> findByName(String name) {
        return reservationDao.selectByName(name);
    }

    public Reservation addReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = timeDao.selectById(timeId);
        Reservation reservation = new Reservation(name, date, time, themeId);

        boolean isAvailable = reservationDao.isAvailable(themeId, date, timeId);

        if (!isAvailable) {
            throw new IllegalArgumentException("[ERROR] 예약할 수 없습니다.");
        }

        return reservationDao.insert(reservation);
    }

    public void update(Long id, String name, LocalDate date, Long timeId) {
        reservationDao.update(id, name, date, timeId);
    }

    public void delete(Long id, String name, LocalDateTime now) {
        Reservation reservation = reservationDao.selectById(id);

        if (!reservation.getName().equals(name)) {
            throw new BusinessException(ErrorCode.RESERVATION_FORBIDDEN);
        }

        if (reservation.isPast(now)) {
            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_PAST);
        }
        reservationDao.delete(id, name);
    }
}
