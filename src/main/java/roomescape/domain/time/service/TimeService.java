package roomescape.domain.time.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.dto.request.TimeCreateRequestDto;
import roomescape.domain.time.dto.response.TimeResponseDto;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class TimeService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;

    public TimeService(ReservationRepository reservationRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public List<TimeResponseDto> getTimes() {
        return timeRepository.findAllTimes()
            .stream()
            .map(Time::toResponseDto)
            .toList();
    }

    public List<TimeResponseDto> getAvailableTimes(LocalDate date, Long themeId) {
        List<Long> reservedTimeIds = reservationRepository.findTimeIdsByDateAndThemeId(date, themeId);

        return timeRepository.findAllTimes()
            .stream()
            .filter(time -> !reservedTimeIds.contains(time.getId()))
            .map(Time::toResponseDto)
            .toList();
    }

    public TimeResponseDto saveTime(TimeCreateRequestDto requestDto) {
        Time time = Time.create(requestDto.startAt());
        return timeRepository.save(time).toResponseDto();
    }

    public void deleteTimeById(Long id) {
        timeRepository.deleteTimeById(id);
    }
}
