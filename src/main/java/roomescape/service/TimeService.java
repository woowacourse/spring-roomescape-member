package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.time.TimeRequest;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class TimeService {

    private final ReservationTimeRepository timeRepository;

    public TimeService(final ReservationTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<TimeResponse> getTimes() {
        return timeRepository.findAll().stream()
                .map(TimeResponse::from)
                .toList();
    }

    public TimeResponse addTime(final TimeRequest timeRequest) {
        ReservationTime parsedTime = timeRequest.toDomain();
        ReservationTime savedTime = timeRepository.save(parsedTime);
        return TimeResponse.from(savedTime);
    }

    public int deleteTime(final Long id) {
        return timeRepository.deleteById(id);
    }
}
