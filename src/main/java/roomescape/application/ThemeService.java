package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.infrastructure.repository.ReservationRepository;
import roomescape.infrastructure.repository.ThemeRepository;
import roomescape.presentation.dto.request.ThemeCreateRequest;
import roomescape.presentation.dto.response.ThemeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ThemeService {

    private static final int POPULAR_THEME_COUNTS = 10;

    private final CurrentTimeService currentTimeService;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(CurrentTimeService currentTimeService,
                        ThemeRepository themeRepository,
                        ReservationRepository reservationRepository) {
        this.currentTimeService = currentTimeService;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> getThemes() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    public ThemeResponse createTheme(ThemeCreateRequest request) {
        Theme theme = Theme.create(request.name(), request.description(), request.thumbnail());
        Theme created = themeRepository.save(theme);
        return ThemeResponse.from(created);
    }

    public void deleteThemeById(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
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
        LocalDate now = currentTimeService.now().toLocalDate();
        List<Theme> themes = themeRepository.findPopularThemeDuringAWeek(POPULAR_THEME_COUNTS, now);
        return ThemeResponse.from(themes);
    }
}
