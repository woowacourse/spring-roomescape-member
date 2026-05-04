package roomescape.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;

    public ReservationTimeService(ReservationTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }


    public TimeResponse createTime(TimeRequest request) {
        ReservationTime time = ReservationTime.of(
            request.startAt()
        );
        ReservationTime saved = timeRepository.save(time);
        return TimeResponse.from(saved);
    }

    public List<TimeResponse> getAllTimes() {
        List<ReservationTime> times = timeRepository.findAll();

        List<TimeResponse> responses = new ArrayList<>();
        for (ReservationTime time : times) {
            TimeResponse response = TimeResponse.from(time);
            responses.add(response);
        }
        return responses;
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
