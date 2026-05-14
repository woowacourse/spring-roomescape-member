package roomescape.reservationTime.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.TimeAvailability;
import roomescape.reservationTime.dao.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao timeDao;

    public ReservationTimeService(ReservationTimeDao timeDao) {
        this.timeDao = timeDao;
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
        timeDao.delete(id);
    }
}
