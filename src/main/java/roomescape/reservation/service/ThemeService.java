package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ThemeRequest;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse add(ThemeRequest request) {
        Theme themeWithoutId = request.toThemeWithoutId();
        Long id = themeRepository.saveAndReturnId(themeWithoutId);

        Theme theme = themeWithoutId.withId(id);
        return ThemeResponse.from(theme);
    }

}
