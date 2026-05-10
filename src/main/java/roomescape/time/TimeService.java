package roomescape.user.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.user.dao.ReservationTimeDao;
import roomescape.user.dto.AvailableTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao timeDao;

    public ReservationTimeService(ReservationTimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<ReservationTime> findAll() {
        return timeDao.selectAll();
    }

    public List<AvailableTimeResponse> findByThemeIdAndDate(Long themeId, LocalDate date) {
        return timeDao.selectByThemeIdAndDate(themeId, date);
    }

    public ReservationTime add(LocalTime startAt) {
        ReservationTime time = new ReservationTime(startAt);
        return timeDao.insert(time);
    }

    public void delete(Long id) {
        timeDao.delete(id);
    }
}
