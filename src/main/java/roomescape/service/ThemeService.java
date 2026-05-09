package roomescape.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.theme.PopularThemeResponseDto;
import roomescape.dto.theme.ThemeRequestDto;
import roomescape.repository.theme.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme addTheme(ThemeRequestDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl());
        return themeRepository.createTheme(theme);
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Map<Theme, Integer> findWeekPopularThemesOrderByRank(final int limit) {
        List<Theme> themes = themeRepository.findWeekPopularThemesOrderByRank(limit);
        Map<Theme, Integer> themesWithRank = new HashMap<>();

        for (int i = 0; i < themes.size(); i++) {
            themesWithRank.put(themes.get(i), i + 1);
        }

        return themesWithRank;
    }
}
