package roomescape.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

    private final TimeDao timeDao;

    public TimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<ReservationTime> findAll() {
        return timeDao.selectAll();
    }

    public List<AvailableTime> findByThemeIdAndDate(Long themeId, LocalDate date) {
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
