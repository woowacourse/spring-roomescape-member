package roomescape.admin.service;

import org.springframework.stereotype.Service;
import roomescape.admin.domain.Theme;
import roomescape.admin.dto.ThemeRequest;
import roomescape.admin.dto.ThemeResponse;
import roomescape.admin.repository.ThemeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(ThemeRequest request) {
        Theme theme = Theme.of(
                request.name(),
                request.description(),
                request.imageUrl()
        );
        Theme saved = themeRepository.save(theme);
        return ThemeResponse.from(saved);
    }

    public List<ThemeResponse> getAllThemes() {
        List<Theme> themes = themeRepository.findAll();

        List<ThemeResponse> responses = new ArrayList<>();
        for (Theme theme : themes) {
            ThemeResponse response = ThemeResponse.from(theme);
            responses.add(response);
        }
        return responses;
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }
}
