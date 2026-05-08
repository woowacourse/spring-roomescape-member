package roomescape.reservationtime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public UserReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ScheduleResponse getSchedules(LocalDate date, Long themeId) {
        List<AvailableTime> schedules = reservationTimeRepository.findAvailableTimes(date, themeId);
        return new ScheduleResponse(themeId, date, schedules);
    }
}
