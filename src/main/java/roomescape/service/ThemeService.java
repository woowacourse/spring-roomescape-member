package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.request.ThemeRequest;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAllThemes();
    }

    public Theme addTheme(ThemeRequest themeRequest) {
        Theme theme = new Theme(themeRequest.getName(), themeRequest.getDescription(), themeRequest.getThumbnail());
        return themeRepository.addTheme(theme);
    }

    public void deleteTheme(long id) {
        themeRepository.deleteTheme(id);
    }
}
