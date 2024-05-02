package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.PopularThemeResponse;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.dto.ThemeSaveRequest;
import roomescape.reservation.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Long save(ThemeSaveRequest themeSaveRequest) {
        Theme theme = themeSaveRequest.toTheme();
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
