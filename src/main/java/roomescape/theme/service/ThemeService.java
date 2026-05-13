package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme createTheme(String name, String description, String thumbnail) {
        Theme theme = new Theme(null, name, description, thumbnail);
        return themeRepository.save(theme);
    }

    @Transactional
    public void removeTheme(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new BusinessException(ErrorCode.THEME_IN_USE);
        }
        themeRepository.remove(id);
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public List<AvailableTime> getAvailableTimes(Long id, LocalDate date){
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Long> availableTimes = themeRepository.findNotAvailableTimes(id, date);

        return reservationTimes.stream()
                .map(t -> AvailableTime.of(
                        t.getId(),
                        t.getStartAt(),
                        !availableTimes.contains(t.getId())))
                .toList();
    }

    public List<Theme> getPopularThemes(int days, int limit) {
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.minusDays(1);
        LocalDate startDate = endDate.minusDays(days);

        return themeRepository.findPopularThemes(startDate, endDate, limit);
    }
}
