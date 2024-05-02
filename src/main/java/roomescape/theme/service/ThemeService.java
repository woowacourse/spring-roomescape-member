package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Long save(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toTheme();
        return themeRepository.save(theme);
    }

    public ThemeResponse findById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        return ThemeResponse.toResponse(theme);
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::toResponse)
                .toList();
    }

    public List<PopularThemeResponse> findPopularThemeLimitTen() {
        List<Theme> popularTheme = themeRepository.findPopularThemeLimitTen();
        return popularTheme.stream()
                .map(PopularThemeResponse::toResponse)
                .toList();
    }

    public void delete(Long id) {
        themeRepository.delete(id);
    }
}
