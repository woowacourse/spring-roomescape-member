package roomescape.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.response.ThemeResponse;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }


    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }
}
