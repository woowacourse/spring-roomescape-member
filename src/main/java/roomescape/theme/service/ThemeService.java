package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<FindThemeResponse> getThemes() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(FindThemeResponse::of)
                .toList();
    }
}
