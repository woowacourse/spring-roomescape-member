package roomescape.domain.time.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.global.exception.ConflictException;
import roomescape.domain.global.exception.ErrorCode;
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
            .map(TimeResponseDto::from)
            .toList();
    }

    public List<TimeResponseDto> getAvailableTimes(LocalDate date, Long themeId) {
        List<Long> reservedTimeIds = reservationRepository.findTimeIdsByDateAndThemeId(date, themeId);

        return timeRepository.findAllTimes()
            .stream()
            .filter(time -> !reservedTimeIds.contains(time.getId()))
            .map(TimeResponseDto::from)
            .toList();
    }

    public TimeResponseDto saveTime(TimeCreateRequestDto requestDto) {
        Time time = Time.create(requestDto.startAt());
        if (timeRepository.existsByStartAt(time.getStartAt())) {
            throw new ConflictException(ErrorCode.TIME_DUPLICATE);
        }

        return TimeResponseDto.from(timeRepository.save(time));
    }

    public void deleteTimeById(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalStateException("요청된 시간을 참조하는 예약이 존재합니다.");
        }
        timeRepository.deleteTimeById(id);
    }
}
