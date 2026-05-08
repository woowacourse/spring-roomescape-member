package roomescape.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import roomescape.user.domain.ReservationTime;
import roomescape.user.dto.TimeRequest;
import roomescape.user.dto.TimeResponse;
import roomescape.user.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;

    public ReservationTimeService(ReservationTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public TimeResponse createTime(TimeRequest request) {
        ReservationTime time = ReservationTime.of(
                request.startAt(),
                request.finishAt()
        );
        ReservationTime saved = timeRepository.save(time);
        return TimeResponse.of(saved);
    }

    public List<TimeResponse> getAllTimes() {
        return timeRepository.findAll().stream()
                .map(TimeResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
