package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.repository.theme.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme addTheme(ThemeCommand themeCommand) {
        return themeRepository.addTheme(themeCommand);
    }

    public List<Theme> getAllTheme() {
        return themeRepository.getAllTheme();
    }

    public void deleteTheme(long id) {
        themeRepository.deleteTheme(id);
    }
}
