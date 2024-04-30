package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.ThemeRequest;
import roomescape.controller.response.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse save(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toEntity();

        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }
}
