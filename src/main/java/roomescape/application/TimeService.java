package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.TimeDto;
import roomescape.domain.ReservationTime;
import roomescape.exception.NotFoundException;
import roomescape.presentation.dto.request.TimeRequest;
import roomescape.application.mapper.ReservationTimeMapper;
import roomescape.domain.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository repository;

    public TimeService(TimeRepository repository) {
        this.repository = repository;
    }

    public List<TimeDto> getAllTimes() {
        List<ReservationTime> reservationTimes = repository.findAll();
        return ReservationTimeMapper.toDtos(reservationTimes);
    }

    public TimeDto registerNewTime(TimeRequest request) {
        ReservationTime newReservationTime = ReservationTimeMapper.toDomain(request);
        Long id = repository.save(newReservationTime);

        ReservationTime saved = ReservationTime.assignId(id, newReservationTime);

        return ReservationTimeMapper.toDto(saved);
    }

    public void deleteTime(Long id) {
        repository.deleteById(id);
    }

    public ReservationTime getTimeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("삭제하려는 id가 존재하지 않습니다, id: " + id));
    }
}
