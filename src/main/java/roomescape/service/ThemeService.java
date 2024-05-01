package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.theme.CreateThemeRequest;
import roomescape.controller.theme.CreateThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<CreateThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(CreateThemeResponse::from)
                .toList();
    }

    public CreateThemeResponse addTheme(final CreateThemeRequest createThemeRequest) {
        final Theme theme = createThemeRequest.toDomain();
        final Theme savedTheme = themeRepository.save(theme);
        return CreateThemeResponse.from(savedTheme);
    }

    public int deleteTheme(final Long id) {
        return themeRepository.delete(id);
    }
}
