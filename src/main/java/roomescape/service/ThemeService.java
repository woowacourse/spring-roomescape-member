package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequestDto;
import roomescape.repository.theme.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme addTheme(ThemeRequestDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl());
        return themeRepository.createTheme(theme);
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteById(id);
    }
}
