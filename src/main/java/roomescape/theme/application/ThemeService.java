package roomescape.theme.application;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.presentation.dto.ThemeRequest;
import roomescape.theme.presentation.dto.ThemeResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class ThemeService {

    private static final int WEEKS_BOUND = 1;
    private static final int DAYS_BOUND = 1;
    private static final int THEME_SIZE_LIMIT = 10;

    private final ThemeRepository repository;

    public ThemeResponse addTheme(ThemeRequest request) {
        Theme theme = repository.save(ThemeRequest.toEntity(request));
        return ThemeResponse.from(theme);
    }

    public void deleteTheme(Long id) {
        if (repository.delete(id) < 1) {
            throw new ThemeNotFoundException("존재하지 않는 테마입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> getThemes() {
        List<Theme> themes = repository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> getWeeksTopThemes() {
        List<Theme> themes = repository.findByReservationCountWithLimit(
                LocalDate.now().minusWeeks(WEEKS_BOUND),
                LocalDate.now().minusDays(DAYS_BOUND),
                THEME_SIZE_LIMIT
        );
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
