package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.time.Time;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.TimeResponse;
import roomescape.repository.TimeRepository;

// TODO: 테스트 추가
@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<TimeResponse> findAllTimes() {
        return timeRepository.findAll()
                .stream()
                .map(TimeResponse::from)
                .toList();
    }

    public TimeResponse createTime(TimeRequest timeRequest) {
        Time time = timeRequest.toTime();
        Time savedTime = timeRepository.save(time);

        return TimeResponse.from(savedTime);
    }

    public void deleteTime(Long id) {
        timeRepository.delete(id);
    }
}
