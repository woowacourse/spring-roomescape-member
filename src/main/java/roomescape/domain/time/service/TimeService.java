package roomescape.domain.time.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.time.dto.request.TimeCreateRequestDTO;
import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<TimeResponseDTO> getTimes() {
        return timeRepository.findAllTimes()
            .stream()
            .map(Time::toResponseDTO)
            .toList();
    }

    public TimeResponseDTO saveTime(TimeCreateRequestDTO requestDTO) {
        Time time = Time.create(requestDTO.startAt());
        return timeRepository.save(time).toResponseDTO();
    }

    public void deleteTimeById(Long id) {
        timeRepository.deleteTimeById(id);
    }
}
