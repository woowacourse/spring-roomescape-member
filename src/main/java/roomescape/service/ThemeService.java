package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAll(boolean showRanking) {
        if (showRanking) {
            LocalDate startDate = LocalDate.now().minusDays(7);
            LocalDate endDate = LocalDate.now().minusDays(1);
            int limitCount = 10;
            return themeRepository.findPopularThemes(startDate, endDate, limitCount).stream()
                    .map(ThemeResponse::from)
                    .toList();
        }
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toDomain();
        Theme createdTheme = themeRepository.create(theme);
        return ThemeResponse.from(createdTheme);
    }

    public void deleteTheme(long id) {
        themeRepository.removeById(id);
    }
}
