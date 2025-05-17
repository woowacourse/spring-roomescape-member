package roomescape.business.service.member;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.presentation.member.dto.ThemeResponseDto;

@Service
public class ReservationThemeService {

    private static final int BEST_THEME_RANGE_DAYS = 7;
    private static final int BEST_THEME_END_OFFSET = 1;
    private static final int TOP_THEME_LIMIT = 10;

    private final ReservationThemeRepository reservationThemeRepository;

    @Autowired
    public ReservationThemeService(ReservationThemeRepository reservationThemeRepository) {
        this.reservationThemeRepository = reservationThemeRepository;
    }

    public List<ThemeResponseDto> getAllThemes() {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.findAll();
        return reservationThemes.stream()
                .map(ThemeResponseDto::toResponse)
                .toList();
    }

    public List<ThemeResponseDto> findBestReservedThemes() {
        LocalDate now = LocalDate.now();
        LocalDate start = calculateStartDate(now);
        LocalDate end = calculateEndDate(now);
        return getBestReservedThemes(start, end)
                .stream()
                .map(ThemeResponseDto::toResponse)
                .toList();
    }

    private List<ReservationTheme> getBestReservedThemes(LocalDate start, LocalDate end) {
        return reservationThemeRepository.findByStartDateAndEndDateOrderByReservedDesc(
                start, end, ReservationThemeService.TOP_THEME_LIMIT);
    }

    private LocalDate calculateStartDate(LocalDate nowDate) {
        return nowDate.minusDays(BEST_THEME_RANGE_DAYS);
    }

    private LocalDate calculateEndDate(LocalDate nowDate) {
        return nowDate.minusDays(BEST_THEME_END_OFFSET);
    }
}
