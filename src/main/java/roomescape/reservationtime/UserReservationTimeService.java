package roomescape.reservationtime;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.ReservationRepository;

@Service
public class UserReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public UserReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findReservationTimes() {
        return reservationTimeRepository.findAll();
    }



    public ScheduleResponse getSchedules(LocalDate date, Long themeId) {
        List<AvailableTimeDto> schedules = reservationTimeRepository.findAvailableTimes(date, themeId);
        return new ScheduleResponse(themeId, date, schedules);
    }
}
