package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

@Service
public class ThemeService {

    private static final LocalDate TOP_THEMES_PERIOD = LocalDate.now().minusDays(7);
    private static final int TOP_THEMES_LIMIT = 10;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> getAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse addTheme(ThemeRequest request) {
        Theme theme = request.toTheme();
        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    public void deleteThemeById(Long id) {
        boolean exist = reservationRepository.existByThemeId(id);
        if (exist) {
            throw new IllegalArgumentException("해당 테마에 예약이 존재합니다.");
        }

        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> getTopThemes() {
        List<Theme> themes = themeRepository.findTopThemesWithinDays(TOP_THEMES_PERIOD, TOP_THEMES_LIMIT);

        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
