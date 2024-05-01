package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toTheme();
        Theme savedTheme = themeRepository.saveTheme(theme);
        return ThemeResponse.fromTheme(savedTheme);
    }

    public void removeTheme(long id) {
        themeRepository.deleteThemeById(id);
    }

    public List<ThemeResponse> findThemes() {
        List<Theme> themes =  themeRepository.findAllThemes();
        return themes.stream()
                .map(ThemeResponse::fromTheme)
                .toList();
    }
}
