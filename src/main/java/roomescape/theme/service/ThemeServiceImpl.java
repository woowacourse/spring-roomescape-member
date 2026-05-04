package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public Theme createTheme(String name, String description, String thumbnail) {
        return themeRepository.save(new Theme(name, description, thumbnail));
    }

    @Override
    public void removeTheme(Long id) {
        themeRepository.remove(id);
    }

    @Override
    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }
}
