package roomescape.domain.time.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.global.exception.BadRequestException;
import roomescape.domain.global.exception.ConflictException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.ErrorDetail;
import roomescape.domain.global.exception.NotFoundException;
import roomescape.domain.global.exception.UnprocessableEntityException;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.request.TimeCreateRequestDto;
import roomescape.domain.time.dto.response.TimeResponseDto;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class TimeService {

    private final Clock clock;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public TimeService(Clock clock, ReservationRepository reservationRepository, ThemeRepository themeRepository,
        TimeRepository timeRepository) {
        this.clock = clock;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public List<TimeResponseDto> getTimes() {
        return timeRepository.findAllTimes()
            .stream()
            .map(TimeResponseDto::from)
            .toList();
    }

    public List<TimeResponseDto> getAvailableTimes(LocalDate date, Long themeId) {
        validateDate(date);
        validateThemeId(themeId);
        List<Long> reservedTimeIds = reservationRepository.findTimeIdsByDateAndThemeId(date,
            themeId);

        return timeRepository.findAllTimes()
            .stream()
            .filter(time -> !reservedTimeIds.contains(time.getId()))
            .filter(time -> !time.isPast(clock))
            .map(TimeResponseDto::from)
            .toList();
    }

    private void validateDate(LocalDate date) {
        LocalDate now = LocalDate.now(clock);
        if (date.isBefore(now)) {
            throw new UnprocessableEntityException(ErrorCode.TIME_INVALID_DATE);
        }
    }

    private void validateThemeId(Long themeId) {
        if (!themeRepository.existsById(themeId)) {
            throw new BadRequestException(ErrorCode.COMMON_INVALID_REQUEST,
                List.of(ErrorDetail.of("themeId", themeId, "요청한 테마 id가 존재하지 않습니다.")));
        }
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
            throw new ConflictException(ErrorCode.TIME_REFERENCED_BY_RESERVATION);
        }
        if (timeRepository.deleteTimeById(id) == 0) {
            throw new NotFoundException(ErrorCode.TIME_NOT_FOUND);
        }
    }
}
