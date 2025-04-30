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
        return themeRepository.addTheme(themeRequestDto);
    }

    public List<Theme> getAllThemes() {
        return themeRepository.getAllTheme();
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteTheme(id);
    }
}
