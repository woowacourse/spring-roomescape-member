package roomescape.domain.theme.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.theme.request.ThemeCreateRequest;
import roomescape.domain.theme.response.ThemeResponse;
import roomescape.domain.theme.response.ThemesResponse;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemesResponse findAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return new ThemesResponse(responses);
    }

    @Transactional
    public ThemeResponse saveTheme(ThemeCreateRequest request) {
        Theme theme = new Theme(
                request.name(),
                request.description(),
                request.thumbnailUrl()
        );

        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    @Transactional
    public void deleteThemeById(Long themeId) {
        themeRepository.deleteById(themeId);
    }
}
