package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;

    public ReservationTimeService(ReservationTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<ReservationTime> getTimes() {
        return timeRepository.findAll();
    }

    @Transactional
    public ReservationTime addTime(ReservationTime time) {
        Long id = timeRepository.save(time);
        return findById(id);
    }

    @Transactional
    public void deleteTime(Long id) {
        timeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ReservationTime findById(Long id) {
        return timeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }
}
