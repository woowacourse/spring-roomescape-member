package roomescape.domain.time.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.request.TimeCreateRequestDto;
import roomescape.domain.time.dto.response.TimeResponseDto;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.error.type.TimeErrorType;
import roomescape.domain.time.mapper.TimeMapper;
import roomescape.domain.time.repository.TimeRepository;
import roomescape.global.error.dto.ParameterErrorResponseDto;
import roomescape.global.error.exception.GeneralException;
import roomescape.global.error.exception.GeneralNotFoundException;

@Service
public class TimeService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public TimeService(ReservationRepository reservationRepository, TimeRepository timeRepository,
        ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<TimeResponseDto> getTimes() {
        return timeRepository.findAllTimes()
            .stream()
            .map(TimeMapper::toResponseDto)
            .toList();
    }

    public List<TimeResponseDto> getAvailableTimes(LocalDate date, Long themeId) {
        if (!themeRepository.existsThemeById(themeId)) {
            throw new GeneralNotFoundException(TimeErrorType.FIELD_RESOURCE_NOT_FOUND,
                List.of(new ParameterErrorResponseDto("themeId", "존재 하지 않는 테마입니다.")));
        }

        List<Long> reservedTimeIds = reservationRepository.findTimeIdsByDateAndThemeId(date, themeId);

        return timeRepository.findAllTimes()
            .stream()
            .filter(time -> !reservedTimeIds.contains(time.getId()))
            .map(TimeMapper::toResponseDto)
            .toList();
    }

    public TimeResponseDto saveTime(TimeCreateRequestDto requestDto) {
        if (timeRepository.existsTimeByStartAt(requestDto.startAt())) {
            throw new GeneralException(TimeErrorType.ALREADY_EXIST_TIME);
        }

        Time time = Time.create(requestDto.startAt());
        return TimeMapper.toResponseDto(timeRepository.save(time));
    }

    public void deleteTimeById(Long id) {
        timeRepository.deleteTimeById(id);
    }
}
