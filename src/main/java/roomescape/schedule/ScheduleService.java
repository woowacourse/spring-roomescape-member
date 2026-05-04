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
        // 1. 전체시간을 ReservationTimeRepository에서 가져오기
        List<ReservationTime> reservations = reservationTimeRepository.findAll();

        // 2. 해당 날짜에 해당 테마로 예약되어있는 정보 ReservationRepository에서 만들기
        List<Long> times = reservationRepository.findByDateAndTheme(date, themeId);

        // 3. 해당 두개를 가지고 Service에서 조합. (schedules List를 만들기 위해서.)
        // 모든 시간을 돌면서 예약에 포함되어있다면 false, 아니라면 true로 schedules 리스트를 만듦

        List<AvailableTimeDto> schedules = new ArrayList<>();

        for (ReservationTime reservationTime : reservations) {
            if(times.contains(reservationTime.id())) {
                schedules.add(new AvailableTimeDto(reservationTime.id(), false));
            }
            else {
                schedules.add(new AvailableTimeDto(reservationTime.id(), true));
            }
        }

        return new ScheduleResponse(themeId, date, schedules);
    }
}
