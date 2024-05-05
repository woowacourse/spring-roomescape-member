package roomescape.service.theme;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.SaveThemeRequest;

@Service
public class ThemeCreateService {

    private final ThemeRepository themeRepository;

    public ThemeCreateService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme createTheme(SaveThemeRequest request) {
        Theme theme = SaveThemeRequest.toEntity(request);
        return themeRepository.save(theme);
    }
}
