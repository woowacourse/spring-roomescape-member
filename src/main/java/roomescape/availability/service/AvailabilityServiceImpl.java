package roomescape.availability.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import roomescape.holiday.repository.HolidayRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.TimeRepository;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final HolidayRepository holidayRepository;
    private final ReservationRepository reservationRepository;

    public AvailabilityServiceImpl(
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            HolidayRepository holidayRepository,
            ReservationRepository reservationRepository
    ) {
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.holidayRepository = holidayRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<ReservationTime> getAvailableTimes(Long themeId, LocalDate date) {
        validateThemeExists(themeId);

        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }

        if (holidayRepository.existsByDate(date)) {
            return List.of();
        }

        Set<Long> reservedTimeIds = new HashSet<>(reservationRepository.findTimeIdsByThemeIdAndDate(themeId, date));
        return timeRepository.findAll()
                .stream()
                .filter(time -> !reservedTimeIds.contains(time.getId()))
                .collect(Collectors.toList());
    }

    private void validateThemeExists(Long themeId) {
        if (themeId == null || !themeRepository.existsById(themeId)) {
            throw new ThemeNotFoundException(themeId);
        }
    }
}
