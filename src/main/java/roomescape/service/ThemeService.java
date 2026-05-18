package roomescape.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.controller.dto.theme.ThemeRequestDto;
import roomescape.repository.theme.ThemeRepository;
import roomescape.service.command.ThemeCommand;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme addTheme(ThemeCommand command) {
        Theme theme = new Theme(command.name(), command.description(), command.imageUrl());
        return themeRepository.createTheme(theme);
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public List<Theme> findWeekPopularThemesOrderByRank(final int limit) {
        return themeRepository.findWeekPopularThemesOrderByRank(limit);
    }
}
