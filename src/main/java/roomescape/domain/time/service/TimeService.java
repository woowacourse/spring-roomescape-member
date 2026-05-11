package roomescape.domain.time.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.dto.request.TimeCreateRequestDTO;
import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.mapper.TimeMapper;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class TimeService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;

    public TimeService(ReservationRepository reservationRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public List<TimeResponseDTO> getTimes() {
        return timeRepository.findAllTimes()
            .stream()
            .map(TimeMapper::toResponseDTO)
            .toList();
    }

    public List<TimeResponseDTO> getAvailableTimes(LocalDate date, Long themeId) {
        List<Long> reservedTimeIds = reservationRepository.findTimeIdsByDateAndThemeId(date, themeId);

        return timeRepository.findAllTimes()
            .stream()
            .filter(time -> !reservedTimeIds.contains(time.getId()))
            .map(TimeMapper::toResponseDTO)
            .toList();
    }

    public TimeResponseDTO saveTime(TimeCreateRequestDTO requestDTO) {
        Time time = Time.create(requestDTO.startAt());
        return TimeMapper.toResponseDTO(timeRepository.save(time));
    }

    public void deleteTimeById(Long id) {
        timeRepository.deleteTimeById(id);
    }
}
