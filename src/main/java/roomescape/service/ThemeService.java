package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

import java.util.List;

@Service
public class ThemeService {

    private final ReservationRepository repository;

    public ThemeService(final ReservationRepository repository) {
        this.repository = repository;
    }

    public List<ThemeResponse> getAll() {
        List<Theme> themes = repository.findAllThemes();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse save(ThemeRequest request) {
        validateThemeName(request);
        Theme theme = new Theme(
                request.name(),
                request.description(),
                request.thumbnail()
        );
        return getThemeResponse(theme);
    }

    public void deleteById(Long id) {
        repository.deleteThemeById(id);
    }

    public List<ThemeResponse> getPopularThemes(int count) {
        List<Theme> themes = repository.findPopularThemes(count);
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private void validateThemeName(ThemeRequest request) {
        if (repository.getThemeCountByName(request.name()) != 0) {
            throw new IllegalArgumentException("[ERROR] 해당 테마 이름이 이미 존재합니다.");
        }
    }

    private ThemeResponse getThemeResponse(Theme theme) {
        return ThemeResponse.from(repository.saveTheme(theme));
    }
}
