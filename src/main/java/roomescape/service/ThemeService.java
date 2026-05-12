package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.domain.Reservations;
import roomescape.domain.Theme;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.TimeAvailabilityDto;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private static final int WEEKLY_TOP_LIMIT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository,
                        ReservationTimeRepository reservationTimeRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional
    public Theme create(String name, String description, String thumbnail) {
        validateDuplicateName(name);
        Theme theme = new Theme(name, description, thumbnail);
        Long id = themeRepository.insert(theme);
        return themeRepository.findBy(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
    }

    private void validateDuplicateName(String name) {
        if (themeRepository.existsByName(name)) {
            throw new ConflictException("이미 추가된 테마입니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new NotFoundException("존재하지 않는 테마입니다.");
        }
        if (reservationRepository.existsByThemeId(id)) {
            throw new ConflictException("해당 테마의 예약이 존재합니다.");
        }
        themeRepository.delete(id);
    }

    public List<Theme> findWeeklyTop() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = startDate.plusDays(6);
        return themeRepository.findPopular(startDate, endDate, WEEKLY_TOP_LIMIT);
    }

    public List<TimeAvailabilityDto> findAvailableTime(Long themeId, LocalDate date) {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        Reservations reservations = new Reservations(
                reservationRepository.findReservationsByThemeAndDate(themeId, date));

        return times.stream()
                .map(time -> new TimeAvailabilityDto(time, reservations.isAvailable(time)))
                .toList();
    }
}
