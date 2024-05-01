package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ThemeRepository;

@Service
@Transactional
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(Theme theme) {
        Theme savedTheme = themeRepository.save(theme);
        return new ThemeResponse(
                savedTheme.getId(), savedTheme.getName(), savedTheme.getDescription(), savedTheme.getThumbnail());
    }
}
