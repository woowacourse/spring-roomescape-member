package roomescape.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public SchedulesResponse findAll(ScheduleRequest request) {
        List<Schedule> schedules = scheduleRepository.findAll(request.themeId(), request.date());
        return SchedulesResponse.from(schedules);
    }
}
