package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.ThemeCommand;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme registerTheme(ThemeCommand command) {
        return themeRepository.save(
                Theme.of(
                        command.name(),
                        command.description(),
                        command.thumbnailUrl()
                )
        );
    }

    @Transactional
    public void removeThemeById(Long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAll();
    }
}
