package roomescape.reservation.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.exception.DeleteThemeException;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.ThemeRequest;
import roomescape.reservation.presentation.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(final ThemeRequest themeRequest) {
        return new ThemeResponse(themeRepository.insert(themeRequest));
    }

    public List<ThemeResponse> getThemes() {
        List<Theme> themes = themeRepository.findAllThemes();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void deleteTheme(final Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new DeleteThemeException("[ERROR] 예약이 이미 존재하는 테마입니다.");
        }

        themeRepository.delete(id);
    }

    public List<ThemeResponse> getPopularThemes() {
        return themeRepository.findPopularThemes().stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
