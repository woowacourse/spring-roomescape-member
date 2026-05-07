package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.repository.theme.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme addTheme(ThemeRequest request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl());
        return themeRepository.createTheme(theme);
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    // TODO: limit 은 너무 쿼리? (count? size?) 근데 count 면 딱 그 개수 만큼 반환이 되어야 할 것 같아서 별로임
    public List<Theme> findWeekPopularThemesOrderByRank(final int limit) {
        return themeRepository.findWeekPopularThemesOrderByRank(limit);
    }
}
