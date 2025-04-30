package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreationRequest;
import roomescape.exception.NotFoundException;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository h2ThemeRepository) {
        this.themeRepository = h2ThemeRepository;
    }

    public long addTheme(ThemeCreationRequest request) {
        Theme theme = Theme.createWithoutId(request.name(), request.description(), request.thumbnail());
        return themeRepository.addTheme(theme);
    }

    public List<Theme> findAllTheme() {
        return themeRepository.findAll();
    }

    public Theme findThemeById(long themeId) {
        return loadThemeById(themeId);
    }

    public void deleteThemeById(long themeId) {
        validateThemeById(themeId);
        themeRepository.deleteById(themeId);
    }

    private Theme loadThemeById(long themeId) {
        Optional<Theme> theme = themeRepository.findById(themeId);
        return theme.orElseThrow(()-> new NotFoundException("[ERROR] ID에 해당하는 테마가 존재하지 않습니다."));
    }

    private void validateThemeById(long themeId) {
        Optional<Theme> theme = themeRepository.findById(themeId);
        if (theme.isEmpty()) {
            throw new NotFoundException("[ERROR] ID에 해당하는 테마가 존재하지 않습니다.");
        }
    }
}
