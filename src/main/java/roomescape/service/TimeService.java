package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {

    private final ReservationTimeRepository timeRepository;

    public TimeService(ReservationTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<ReservationTimeResponse> getTimes() {
        return timeRepository.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    @Transactional
    public ReservationTimeResponse addTime(ReservationTime time) {
        Long id = timeRepository.save(time);
        return new ReservationTimeResponse(new ReservationTime(id, time.getStartAt()));
    }

    @Transactional
    public void deleteTime(Long id) {
        timeRepository.deleteById(id);
    }
}
