package roomescape.reservation.application.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.presentation.dto.ThemeRequest;
import roomescape.reservation.presentation.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(final ThemeRequest themeRequest) {
        Long themeId = themeRepository.insert(themeRequest);
        return new ThemeResponse(
                themeId,
                themeRequest.getName(),
                themeRequest.getDescription(),
                themeRequest.getThumbnail()
        );
    }
}
