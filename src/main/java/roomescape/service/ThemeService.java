package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreationRequest;
import roomescape.exception.NotFoundException;
import roomescape.repository.H2ThemeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository h2ThemeRepository;

    public ThemeService(final ThemeRepository h2ThemeRepository) {
        this.h2ThemeRepository = h2ThemeRepository;
    }

    public long addTheme(ThemeCreationRequest request) {
        Theme theme = Theme.createWithoutId(request.name(), request.description(), request.thumbnail());
        return h2ThemeRepository.addTheme(theme);
    }

    public Theme findThemeById(long themeId) {

        return loadThemeById(themeId);
    }

    private Theme loadThemeById(long themeId) {
        Optional<Theme> theme = h2ThemeRepository.findById(themeId);
        return theme.orElseThrow(()-> new NotFoundException("[ERROR] ID에 해당하는 테마가 존재하지 않습니다."));
    }
}
