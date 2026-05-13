package roomescape.reservationtime;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public UserReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findReservationTimes() {
        return reservationTimeRepository.findAll();
    }


    public ScheduleResponse getSchedules(Long themeId, LocalDate date) {
        List<AvailableTime> schedules = reservationTimeRepository.findAvailableTimes(themeId, date);
        return new ScheduleResponse(themeId, date, schedules);
    }
}
