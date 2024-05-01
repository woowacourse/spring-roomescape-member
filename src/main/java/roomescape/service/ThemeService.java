package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse createTheme(ThemeRequest request) {
        Theme theme = themeRepository.save(new Theme(request.name(), request.description(), request.thumbnail()));

        return ThemeResponse.from(theme);
    }
}
