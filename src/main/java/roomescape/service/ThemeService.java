package roomescape.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.theme.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse addTheme(ThemeAddRequest themeAddRequest) {
        Theme theme = themeRepository.save(themeAddRequest.toTheme());
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> findThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findTrendingThemes(Long limit) {
        LocalDate now = LocalDate.now();
        LocalDate trendingStatsStart = now.minusDays(7);
        LocalDate trendingStatsEnd = now.minusDays(1);

        return themeRepository.findTrendings(trendingStatsStart, trendingStatsEnd, limit)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse getTheme(Long id) {
        return ThemeResponse.from(getValidTheme(id));
    }

    private Theme getValidTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다. id = " + id));
    }

    public void deleteTheme(Long id) {
        themeRepository.delete(id);
    }
}
