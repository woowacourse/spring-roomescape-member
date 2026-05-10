package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.repository.theme.ThemeRepository;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme addTheme(ThemeRequest request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl());
        return themeRepository.createTheme(theme);
    }

    public void deleteThemeById(final long id) {
        themeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Theme> findWeekPopularThemesOrderByRank(final int limit) {
        return themeRepository.findWeekPopularThemesOrderByRank(limit);
    }
}
