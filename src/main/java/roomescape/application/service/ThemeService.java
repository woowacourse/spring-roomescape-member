package roomescape.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.exception.ReservationExistException;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.exception.ThemeDuplicatedException;
import roomescape.domain.model.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.presentation.dto.request.ThemeRequest;
import roomescape.presentation.dto.response.ThemeResponse;

import java.util.List;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(final ReservationRepository reservationRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public ThemeResponse save(ThemeRequest request) {
        validateThemeName(request);
        Theme theme = new Theme(
                request.name(),
                request.description(),
                request.thumbnail()
        );
        Theme savedTheme = themeRepository.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    @Transactional
    public void deleteById(Long id) {
        boolean isThemeInUse = reservationRepository.existByThemeId(id);
        if (isThemeInUse) {
            throw new ReservationExistException();
        }
        int count = themeRepository.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    public List<ThemeResponse> getPopularThemes(int count) {
        List<Theme> themes = themeRepository.findPopular(count);
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private void validateThemeName(ThemeRequest request) {
        if (themeRepository.existByName(request.name())) {
            throw new ThemeDuplicatedException();
        }
    }
}
