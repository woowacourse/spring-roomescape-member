package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.AddThemeDto;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public long addTheme(AddThemeDto addThemeDto) {
        Theme theme = addThemeDto.toEntity();
        return themeRepository.add(theme);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public void deleteThemeById(long id) {
        themeRepository.deleteById(id);
    }
}
