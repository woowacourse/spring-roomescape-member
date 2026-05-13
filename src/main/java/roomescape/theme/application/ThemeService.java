package roomescape.theme.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.application.exception.DuplicateThemeException;
import roomescape.theme.application.exception.ThemeInUseException;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ThemeService {

    private static final int DELETE_ROW_COUNTS = 0;
    private static final int WEEKS_BOUND = 1;
    private static final int DAYS_BOUND = 1;
    private static final int THEME_SIZE_LIMIT = 10;

    private final Clock clock;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public Theme addTheme(Theme theme) {
        if (themeRepository.existsByName(theme.getName())) {
            throw new DuplicateThemeException("이미 존재하는 테마입니다.");
        }
        return themeRepository.save(theme);
    }

    public void deleteTheme(Long id) {
        if (reservationRepository.existsByTheme(id)) {
            throw new ThemeInUseException("해당 테마의 예약이 존재합니다.");
        }
        if (themeRepository.delete(id) == DELETE_ROW_COUNTS) {
            throw new ThemeNotFoundException("존재하지 않는 테마입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Theme> getWeeksTopThemes() {
        return themeRepository.findByReservationCountWithLimit(
                LocalDate.now(clock).minusWeeks(WEEKS_BOUND),
                LocalDate.now(clock).minusDays(DAYS_BOUND),
                THEME_SIZE_LIMIT
        );
    }
}
