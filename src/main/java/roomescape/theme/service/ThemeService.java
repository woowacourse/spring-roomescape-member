package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.infrastructure.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(final ThemeRequest request) {
        Long id = themeRepository.save(new Theme(null, request.name(), request.description(), request.thumbnail()));
        Theme findTheme = themeRepository.findById(id);

        return ThemeResponse.from(findTheme);
    }

    public void deleteThemeById(final Long id) {
        int count = themeRepository.deleteById(id);
        validateIsExistsReservationTimeId(count);
    }

    private void validateIsExistsReservationTimeId(final int count) {
        if (count == 0) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
