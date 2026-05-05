package roomescape.user.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Schedule;
import roomescape.user.dao.ScheduleDao;

@Service
public class ScheduleService {

    private final ScheduleDao scheduleDao;

    public ScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    public List<Schedule> findById(Long id, LocalDate date) {
        return scheduleDao.selectById(id, date);
    }
}
