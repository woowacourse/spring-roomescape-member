package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.model.ReservationTime;
import roomescape.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<TimeResponse> readTimeAll() {
        List<ReservationTime> times = timeRepository.findAll();
        return times.stream()
                .map(TimeResponse::from)
                .collect(Collectors.toList());
    }

    public void removeTime(Long id) {
        timeRepository.removeById(id);
    }

    public TimeResponse registerTime(TimeRequest timeRequest) {
        ReservationTime reservationTime = timeRepository.saveTime(timeRequest.getStartAt());
        return TimeResponse.from(reservationTime);
    }

    public TimeResponse findById(Long id) {
        ReservationTime reservationTime = timeRepository.selectById(id);
        return TimeResponse.from(reservationTime);
    }
}
