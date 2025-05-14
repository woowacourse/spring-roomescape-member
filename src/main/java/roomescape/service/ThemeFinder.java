package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

@Component
public class ThemeFinder {
    private final ThemeRepository themeRepository;

    public ThemeFinder(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme getThemeById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다. id: " + id));
    }

}
