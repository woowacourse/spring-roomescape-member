package roomescape.theme.application;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.presentation.dto.ThemeResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class ThemeService {

    private static final int NONE_EFFECTED = 0;
    private static final int WEEKS_BOUND = 1;
    private static final int DAYS_BOUND = 1;
    private static final int THEME_SIZE_LIMIT = 10;

    private final ThemeRepository themeRepository;

    public Theme addTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteTheme(Long id) {
        if (themeRepository.delete(id) == NONE_EFFECTED) {
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
                LocalDate.now().minusWeeks(WEEKS_BOUND),
                LocalDate.now().minusDays(DAYS_BOUND),
                THEME_SIZE_LIMIT
        );
    }
}
