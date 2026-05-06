package roomescape.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.ReservationRepository;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeRepository;

@Service
public class ScheduleService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ScheduleService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ScheduleResponse getSchedules(LocalDate date, Long themeId) {
        List<ReservationTime> reservations = reservationTimeRepository.findAll();

        List<Long> times = reservationRepository.findByDateAndTheme(date, themeId);
        List<AvailableTimeDto> schedules = new ArrayList<>();

        for (ReservationTime reservationTime : reservations) {
            if(times.contains(reservationTime.id())) {
                schedules.add(new AvailableTimeDto(reservationTime.id(), reservationTime.startAt(), false));
            }
            else {
                schedules.add(new AvailableTimeDto(reservationTime.id(), reservationTime.startAt(), true));
            }
        }

        return new ScheduleResponse(themeId, date, schedules);
    }
}
