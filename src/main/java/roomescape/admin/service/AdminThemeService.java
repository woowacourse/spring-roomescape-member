package roomescape.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.doamin.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@RequiredArgsConstructor
public class AdminThemeService {
    private final ThemeRepository themeRepository;

    public long saveTheme(String name, String description, String thumbnailUrl) {
        return themeRepository.save(Theme.of(name, description, thumbnailUrl));
    }

    public void deleteTheme(long id) {
        themeRepository.remove(id);
    }
}
