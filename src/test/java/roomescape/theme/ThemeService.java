package roomescape.theme;

import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;

public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }


    public ThemeResponse saveTheme(ThemeCreateRequest request) {
        Theme theme = request.toEntity();

        return ThemeResponse.from(themeRepository.save(theme));
    }
}
