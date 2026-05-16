package roomescape.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, ReservationRepository reservationRepository) {
        this.scheduleRepository = scheduleRepository;
        this.reservationRepository = reservationRepository;
    }

    public SchedulesResponse findAll(ScheduleRequest request) {
        List<Schedule> schedules = scheduleRepository.findAll(request.themeId(), request.date());
        return SchedulesResponse.from(schedules);
    }

    @Transactional
    public void delete(Long id) {
        if (reservationRepository.existsByScheduleId(id)) {
            throw new IllegalArgumentException("예약이 존재하는 스케줄은 삭제할 수 없습니다.");
        }

        scheduleRepository.delete(id);
    }
}
