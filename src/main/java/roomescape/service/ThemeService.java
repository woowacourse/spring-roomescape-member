package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.theme.ThemeRequest;
import roomescape.controller.theme.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse addTheme(final ThemeRequest themeRequest) {
        final Theme theme = themeRequest.toDomain();
        final Theme savedTheme = themeRepository.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    public int deleteTheme(final Long id) {
        return themeRepository.delete(id);
    }
}
