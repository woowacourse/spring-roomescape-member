package roomescape.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.request.ThemeRequest;
import roomescape.application.dto.response.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.domain.exception.EntityNotFoundException;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme savedTheme = themeRepository.create(request.toTheme());
        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테마 입니다."));
        themeRepository.deleteById(theme.getId());
    }

    public List<ThemeResponse> findPopularThemes() {
        LocalDate today = LocalDate.now(clock);
        return themeRepository.findPopularThemesDateBetween(
                        today.minusDays(7),
                        today.minusDays(1),
                        10)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
