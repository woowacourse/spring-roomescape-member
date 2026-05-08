package roomescape.user.service;

import java.util.List;

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
        List<ReservationTime> times = timeRepository.findAll();

        return times.stream()
            .map(TimeResponse::of)
            .toList();
    }

    public void deleteById(Long id) {
        validateReservationId(id);
        timeRepository.deleteById(id);
    }

    private void validateReservationId(Long id) {
        boolean isValidId = timeRepository.existsById(id);
        if (!isValidId) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 Id입니다");
        }
    }
}
