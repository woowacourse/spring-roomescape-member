package roomescape.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.ReservationRepository;
import roomescape.infrastructure.ThemeRepository;
import roomescape.presentation.dto.request.ThemeCreateRequest;
import roomescape.presentation.dto.response.ThemeResponse;
import roomescape.domain.Theme;

@Service
public class ThemeService {

    private static final int POPULAR_THEME_COUNTS = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

    public List<ThemeResponse> getThemes() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    public ThemeResponse createTheme(ThemeCreateRequest request) {
        Theme theme = themeRepository.save(request.name(), request.description(), request.thumbnail());
        return ThemeResponse.from(theme);
    }

    public void deleteThemeById(Long id) {
        if (reservationRepository.existReservationByThemeId(id)) {
            throw new IllegalArgumentException("[ERROR] 해당 테마에 예약이 존재하여 삭제할 수 없습니다.");
        }
        Theme theme = findThemeById(id);
        themeRepository.deleteById(theme.getId());
    }

    public Theme findThemeById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 해당 테마가 존재하지 않습니다."));
    }

    public List<ThemeResponse> getPopularThemes() {
        List<Theme> themes = themeRepository.findPopularThemeDuringAWeek(POPULAR_THEME_COUNTS, LocalDate.now(clock));
        return ThemeResponse.from(themes);
    }
}
