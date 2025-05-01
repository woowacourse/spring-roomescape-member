package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequestDto;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme addTheme(ThemeRequestDto themeRequestDto) {
        Theme theme = new Theme(null, themeRequestDto.name(), themeRequestDto.description(),
                themeRequestDto.thumbnail());
        return themeRepository.addTheme(theme);
    }

    public List<Theme> getAllThemes() {
        return themeRepository.getAllTheme();
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteTheme(id);
    }

    public Theme getThemeById(Long id) {
        return themeRepository.findById(id);
    }
}
