package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int POPULAR_THEMES_LOOK_BACK_DAYS = 7;
    private static final int POPULAR_THEMES_LIMIT_COUNT = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllTheme() {
        return themeRepository.findAll();
    }

    public List<ThemeResponse> findPopularTheme() {
        LocalDate periodEnd = LocalDate.now();
        LocalDate periodStart = periodEnd.minusDays(POPULAR_THEMES_LOOK_BACK_DAYS);

        return themeRepository.findByPeriodOrderByReservationCount(periodStart, periodEnd, POPULAR_THEMES_LIMIT_COUNT)
                .stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public ThemeResponse saveTheme(ThemeAddRequest themeAddRequest) {
        Theme saved = themeRepository.save(themeAddRequest.toEntity());
        return new ThemeResponse(saved);
    }

    public void removeTheme(Long id) {
        themeRepository.deleteById(id);
    }
}
