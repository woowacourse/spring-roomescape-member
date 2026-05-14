package roomescape.reservationTime.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservationTime.dao.ReservationTimeDao;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.TimeAvailability;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao timeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTime> findAll() {
        return timeDao.selectAll();
    }

    public List<TimeAvailability> findByThemeIdAndDate(Long themeId, LocalDate date) {
        return timeDao.selectByThemeIdAndDate(themeId, date);
    }

    public ReservationTime addReservationTime(LocalTime startAt) {
        ReservationTime time = new ReservationTime(startAt);
        return timeDao.insert(time);
    }

    public void deleteById(Long id) {
        if (reservationDao.existsByTimeId(id)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_IN_USE);
        }
        timeDao.delete(id);
    }
}
