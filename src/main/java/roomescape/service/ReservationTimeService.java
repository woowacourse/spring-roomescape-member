package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationTimeAvailabilityResponseDto;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Transactional(readOnly = true)
@Service
public class ReservationTimeService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository,
                                  ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationTimeResponseDto create(ReservationTimeRequestDto requestDto) {
        ReservationTime reservationTime = reservationTimeRepository.create(requestDto.toEntity());
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public List<ReservationTimeResponseDto> findAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public List<ReservationTimeAvailabilityResponseDto> findAvailabilityByDateAndTheme(
            LocalDate date, Long themeId) {
        Theme theme = findTheme(themeId);

        List<ReservationTime> allReservationTimes = reservationTimeRepository.findAll();
        List<Long> bookedTimeIds = reservationTimeRepository.findBookedTimeIdsByDateAndTheme(date, theme.getId());

        return allReservationTimes.stream()
                .map(reservationTime -> {
                    if (bookedTimeIds.contains(reservationTime.getId())) {
                        return ReservationTimeAvailabilityResponseDto.from(reservationTime, false);
                    }
                    return ReservationTimeAvailabilityResponseDto.from(reservationTime, true);
                }).toList();
    }

    private Theme findTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));
    }

    @Transactional
    public void delete(Long id) {
        validateTimeNotInUse(id);

        reservationTimeRepository.delete(id);
    }

    private void validateTimeNotInUse(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new CustomException(ErrorCode.RESERVATION_TIME_IN_USE);
        }
    }
}
