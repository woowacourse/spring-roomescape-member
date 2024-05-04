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

    public List<ThemeResponse> findAll(boolean showRanking) {
        if (showRanking) {
            LocalDate startDate = LocalDate.now().minusDays(7);
            LocalDate endDate = LocalDate.now().minusDays(1);
            int limitCount = 10;
            return themeRepository.findPopularThemes(startDate, endDate, limitCount).stream()
                    .map(ThemeResponse::from)
                    .toList();
        }
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
