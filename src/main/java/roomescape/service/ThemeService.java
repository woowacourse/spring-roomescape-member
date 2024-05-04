package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ThemeRepository;
import roomescape.service.request.ThemeRequest;
import roomescape.service.response.ThemeResponse;

@Service
public class ThemeService {

    private static final int POPULAR_THEMES_START_DAYS_OF_SUBTRACT = 7;
    private static final int POPULAR_THEMES_END_DAYS_OF_SUBTRACT = 1;
    private static final int POPULAR_THEMES_LIMIT_COUNT = 10;

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = request.toDomain();
        Theme createdTheme = themeRepository.save(theme);

        return ThemeResponse.from(createdTheme);
    }

    public void deleteTheme(long id) {
        if (reservationRepository.hasByThemeId(id)) {
            throw new IllegalStateException("해당 테마를 사용하는 예약이 존재합니다.");
        }

        themeRepository.removeById(id);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopularThemes() {
        LocalDate startDate = LocalDate.now().minusDays(POPULAR_THEMES_START_DAYS_OF_SUBTRACT);
        LocalDate endDate = LocalDate.now().minusDays(POPULAR_THEMES_END_DAYS_OF_SUBTRACT);

        return themeRepository.findPopularThemes(startDate, endDate, POPULAR_THEMES_LIMIT_COUNT).stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
