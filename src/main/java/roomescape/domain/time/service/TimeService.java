package roomescape.domain.time.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.global.exception.custom.BusinessException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.request.TimeCreateRequestDto;
import roomescape.domain.time.dto.response.TimeResponseDto;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class TimeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public TimeService(ReservationRepository reservationRepository, ThemeRepository themeRepository,
        TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public List<TimeResponseDto> getTimes() {
        return timeRepository.findAllTimes()
            .stream()
            .map(TimeResponseDto::from)
            .toList();
    }

    @Transactional
    public List<TimeResponseDto> getAvailableTimes(LocalDate date, Long themeId, LocalDateTime now) {
        validateDate(date, now.toLocalDate());
        validateThemeId(themeId);
        List<Long> reservedTimeIds = reservationRepository.findTimeIdsByDateAndThemeId(date,
            themeId);

        return timeRepository.findAllTimes()
            .stream()
            .filter(time -> !reservedTimeIds.contains(time.getId()))
            .filter(time -> !time.isPast(now.toLocalTime()))
            .map(TimeResponseDto::from)
            .toList();
    }

    private void validateDate(LocalDate date, LocalDate now) {
        if (date.isBefore(now)) {
            throw new BusinessException(ErrorCode.TIME_INVALID_DATE);
        }
    }

    private void validateThemeId(Long themeId) {
        if (!themeRepository.existsById(themeId)) {
            throw new BusinessException(ErrorCode.COMMON_INVALID_REQUEST,
                ErrorDetail.of("themeId", themeId, "요청한 테마 id가 존재하지 않습니다."));
        }
    }

    @Transactional
    public TimeResponseDto saveTime(TimeCreateRequestDto requestDto) {
        Time time = Time.create(requestDto.startAt());
        if (timeRepository.existsByStartAt(time.getStartAt())) {
            throw new BusinessException(ErrorCode.TIME_DUPLICATE);
        }

        return TimeResponseDto.from(timeRepository.save(time));
    }

    @Transactional
    public void deleteTimeById(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new BusinessException(ErrorCode.TIME_REFERENCED_BY_RESERVATION);
        }
        if (timeRepository.deleteTimeById(id) == 0) {
            throw new BusinessException(ErrorCode.TIME_NOT_FOUND);
        }
    }
}
