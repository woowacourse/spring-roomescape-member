package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ResourceNotExistException;

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
        Theme savedTheme = repository.saveTheme(theme);
        return ThemeResponse.from(savedTheme);
    }

    public void deleteById(Long id) {
        boolean isThemeInUse = repository.existReservationByThemeId(id);
        if (isThemeInUse) {
            throw new IllegalArgumentException("[ERROR] 해당 테마에 대한 예약이 존재하기 때문에 삭제할 수 없습니다.");
        }
        int count = repository.deleteThemeById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    public List<ThemeResponse> getPopularThemes(int count) {
        List<Theme> themes = repository.findPopularThemes(count);
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private void validateThemeName(ThemeRequest request) {
        if (repository.existByName(request.name())) {
            throw new IllegalArgumentException("[ERROR] 해당 테마 이름이 이미 존재합니다.");
        }
    }
}
