package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.TimeDto;
import roomescape.domain.repository.dto.AvailableTimesData;

@Service
public class UserReservationService {

    private final TimeService timeService;

    public UserReservationService(TimeService timeService) {
        this.timeService = timeService;
    }

    public List<AvailableTimesData> getAvailableTimes(LocalDate date, Long themeId) {
        List<TimeDto> times = timeService.getAllTimes();
        List<TimeDto> bookedTimes = timeService.getTimesBy(date, themeId);

        return times.stream()
                .map(timeDto -> {
                    boolean alreadyBooked = bookedTimes.contains(timeDto);
                    return new AvailableTimesData(timeDto.id(), timeDto.startAt(), alreadyBooked);
                })
                .toList();
    }
}
