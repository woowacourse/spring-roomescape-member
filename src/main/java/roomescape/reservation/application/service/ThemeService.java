package roomescape.reservation.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.domain.Theme;
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

    public List<ThemeResponse> getThemes() {
        List<Theme> themes = themeRepository.findAllThemes();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
