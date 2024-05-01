package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toTheme();
        Theme savedTheme = themeRepository.save(theme);
        
        return ThemeResponse.from(savedTheme);
    }
}
