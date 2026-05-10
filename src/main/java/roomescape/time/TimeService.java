package roomescape.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.time.dto.AvailableTimeResponse;

@Service
public class TimeService {

    private final TimeDao timeDao;

    public TimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<Time> findAll() {
        return timeDao.selectAll();
    }

    public List<AvailableTimeResponse> findByThemeIdAndDate(Long themeId, LocalDate date) {
        return timeDao.selectByThemeIdAndDate(themeId, date);
    }

    public Time add(LocalTime startAt) {
        Time time = new Time(startAt);
        return timeDao.insert(time);
    }

    public void delete(Long id) {
        timeDao.delete(id);
    }
}
