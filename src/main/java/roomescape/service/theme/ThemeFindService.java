package roomescape.service.theme;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeFindService {
    private final ThemeRepository themeRepository;

    public ThemeFindService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findThemes() {
        return themeRepository.findAll();
    }
}
