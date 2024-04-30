package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.response.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.util.List;

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
}
