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

    private static final int POPULAR_THEME_LIMIT_COUNT = 10;
    private static final int POPULAR_THEME_START_DAY = 7;
    private static final int POPULAR_THEME_END_DAY = 1;

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toDomain();
        Theme createdTheme = themeRepository.create(theme);

        return ThemeResponse.from(createdTheme);
    }

    public void deleteTheme(long id) {
        if (reservationRepository.hasByThemeId(id)) {
            throw new IllegalStateException("해당 테마를 사용하는 예약이 존재합니다.");
        }

        if (!themeRepository.removeById(id)) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다. id를 확인하세요.");
        }
    }

    public List<ThemeResponse> findAll(boolean showRanking) {
        if (showRanking) {
            LocalDate startDate = LocalDate.now().minusDays(POPULAR_THEME_START_DAY);
            LocalDate endDate = LocalDate.now().minusDays(POPULAR_THEME_END_DAY);

            return themeRepository.findPopularThemes(startDate, endDate, POPULAR_THEME_LIMIT_COUNT).stream()
                    .map(ThemeResponse::from)
                    .toList();
        }
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
